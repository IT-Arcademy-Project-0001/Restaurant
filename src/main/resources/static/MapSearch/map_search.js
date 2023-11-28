var markersCategory = {}; // // 카테고리로 검색한 마커를 담을 객체입니다

//위의 선언을 통해 아래와 같은 객체 내부에 동적인 배열구성이 가능해진다.
//markersCategory[1] = [];
//markersCategory[2] = [];
//markersCategory[3] = [];
//...

var markers = []; // // 마커를 담을 배열입니다

var currCategory = ''; // 현재 선택된 카테고리를 가지고 있을 변수입니다

var defaultLat = 36.353720; // 기본 위도 값
var defaultLng = 127.341445; // 기본 경도 값

var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(defaultLat, defaultLng),
        level: 5 // 지도의 확대 레벨
    };

// HTML5의 geolocation으로 사용할 수 있는지 확인합니다
if (navigator.geolocation) {

    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
    navigator.geolocation.getCurrentPosition(function(position) {

        var lat = position.coords.latitude, // 위도
            lon = position.coords.longitude; // 경도

        var locPosition = new kakao.maps.LatLng(lat, lon); // geolocation으로 얻어온 좌표
        map.setCenter(locPosition);

        // 마커와 인포윈도우를 표시합니다 (사실상 제거해도 상관없음)
        var message = '<div style="text-align: center; padding:5px;">현재 GPS 위치입니다</div>'; // 인포윈도우에 표시될 내용입니다
        currentDisplayMarker(locPosition, message);

      }, function(error) {

      // GeoLocation 허용하지 않았을 때.
            alert('GPS 기능이 활성화되지 않았습니다.');
            var locPosition = new kakao.maps.LatLng(36.353720, 127.341445) // 임의 위치의 지도의 중심좌표 (현재 유성온천역, 향후 서울역)
            map.setCenter(locPosition);

      });
}

// 현재 GPS상 위치표시용 마커  (사실상 제거해도 상관없음)
function currentDisplayMarker(locPosition, message) {

    // 마커를 생성합니다
    var marker = new kakao.maps.Marker({
        map: map,
        position: locPosition
    });

    var iwContent = message; // 인포윈도우에 표시할 내용

    // 인포윈도우를 생성합니다
    var infowindow = new kakao.maps.InfoWindow({
        content : iwContent
    });

    // 인포윈도우를 마커위에 표시합니다
    infowindow.open(map, marker);

    // 지도 중심좌표를 접속위치로 변경합니다
    map.setCenter(locPosition);
}

// 지도를 생성합니다
var map = new kakao.maps.Map(mapContainer, mapOption);

// 장소 검색 객체를 생성합니다
var ps = new kakao.maps.services.Places();

// 지도에 idle 이벤트를 등록합니다
kakao.maps.event.addListener(map, 'idle', mySearchPlaces);

// 각 카테고리에 클릭 이벤트를 등록합니다
addCategoryClickEvent();

// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다 (마커기준 z방향으로 1떨어진 위치)
var infowindow = new kakao.maps.InfoWindow({zIndex:1});
var simpleinfowindow = new kakao.maps.InfoWindow({zIndex:1});

document.getElementById("searchkeyword").addEventListener("click", function() {
    toggleMenuWrap();
    // 키워드로 장소를 검색합니다.
    searchPlaces();
});

// 메뉴 스타일 토글 함수
function toggleMenuWrap() {
    var keywordValue = document.getElementById("keyword").value;
    var menuWrap = document.getElementById("menu_wrap");
    var menuWrap2 = document.getElementById("menu_wrap2");

    if (keywordValue.trim() == "") {
        // 키워드가 존재하지 않을 때
        menuWrap.style.display = "none";
        menuWrap2.style.display = "block";
    } else {
        // 키워드가 있을 때
        menuWrap.style.display = "block";
        menuWrap2.style.display = "none";
    }
}

// 키워드 검색을 요청하는 함수입니다
function searchPlaces() {

    var keyword = document.getElementById('keyword').value;

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }

    // 장소 검색시 "검색" 버튼이 눌러지게 하기 위한 함수
    initPressSearchButton();

    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
    ps.keywordSearch(keyword, placesSearchCB);
}

function initPressSearchButton() {
    var searchButton = document.getElementById('BK9');
    if (!searchButton.classList.contains('on')) {
           searchButton.classList.add('on');
        }
}

// idle 이벤트 (ajax 실시간 갱신으로 자체 DB 업데이트)
function mySearchPlaces() {

    var elementDo = document.getElementById(currCategory);
    var dataOrder = elementDo ? parseInt(elementDo.getAttribute('data-order'), 10) || 0 : 0;

    $.ajax({
        url: '/place/search',
        type: 'GET',
        dataType: 'json',
        data: { // 쿼리 문자열로 변환, URL에 포함시켜 서버로 전달.
                latitude: map.getCenter().getLat(),
                longitude: map.getCenter().getLng(),
                order: dataOrder
            },
        success: function (data) {
                // 서버에서 받아온 데이터를 이용하여 마커 생성
                data.forEach(searchResult2 => {

                    // 마커를 생성하고 지도에 표시합니다
                    var marker = addMarkerCategory(new kakao.maps.LatLng(searchResult2.locationLat, searchResult2.locationLng), dataOrder);
                });

        },
        error: function (error) {
            console.error('Error:', error);
        }
    });

}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {

        // 정상적으로 검색이 완료됐으면
        // 검색 목록과 마커를 표출합니다
        displayPlaces(data);

        // 페이지 번호를 표출합니다
        displayPagination(pagination);

    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {

        alert('검색 결과가 존재하지 않습니다.');
        return;

    } else if (status === kakao.maps.services.Status.ERROR) {

        alert('검색 결과 중 오류가 발생했습니다.');
        return;

    }

}

// 검색 결과 목록과 마커를 표출하는 함수입니다
function displayPlaces(places) {

    var listEl = document.getElementById('placesList'),
    menuEl = document.getElementById('menu_wrap'),
    fragment = document.createDocumentFragment(),
    bounds = new kakao.maps.LatLngBounds(),
    listStr = '';

    // 검색 결과 목록에 추가된 기존 항목들을 제거합니다
    removeAllChildNods(listEl);

    // 지도에 표시되고 있는 기존 마커를 제거합니다 (초기화 로직 포함)
    removeMarker();

    // 기존 검색장소의 마커를 제거 후 초기화 합니다. (순서 중요, 마커위치 정보를 제거해버리면 마커를 제거할 수 없게됨)
    // ajax에 의해 식당과 추천 장소는 지속적으로 갱신되지만, 검색장소는 검색한 시점에서 배열이 저장되므로 검색이후 삭제되면 빈배열로 남음.
    // 기존 removeMarker()에서 처럼 마커제거와 초기화 로직을 모두 실행하기 어려움. 하드코딩으로 구현하였음.
    // 페이지네이션에 displayPlaces 함수 실행이 포함되어 있으며 그 시점에서 페이지에 담긴 마커와 장소정보를 제거 및 초기화하게됨.
    removeMarkerCategory(1);
    markersCategory[1] = [];

    for ( var i=0; i < places.length; i++ ) {

        // 마커를 생성하고 지도에 표시합니다
        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
            marker = addMarker(placePosition, i),
            itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
        // LatLngBounds 객체에 좌표를 추가합니다
        bounds.extend(placePosition);

        // 마커와 검색결과 항목에 mouseover 했을때
        // 해당 장소에 인포윈도우에 장소명을 표시합니다
        // mouseout 했을 때는 인포윈도우를 닫습니다
        // 마커, 장소이름(기본예제의 title), 도로명주소, 지번주소명, 위도(세로선), 경도(가로선)
        (function(map, marker, pname, praddress, paddress, plat, plng) {

            // 맵을 클릭하면 발생하는 이벤트
            kakao.maps.event.addListener(map, 'click', function() {
                infowindow.close();
                simpleinfowindow.close();
            });

            // 마우스로 마커를 클릭하면 발생하는 이벤트
            kakao.maps.event.addListener(marker, 'click', function() {

                // 0. 간단 정보창 닫기
                simpleinfowindow.close();

                // 1. 정보창 표시
                displayInfowindow(marker, pname, praddress, paddress);

                // 2. 주소 정보들을 text 영역으로 전송 (hidden 사용)

                // 2. 길찾기 Get쿼리 추가 (탐험하기로 통합)

            })

            // 마우스를 마커 위에 두면 발생하는 이벤트
            kakao.maps.event.addListener(marker, 'mouseover', function() {
                displaysimpleInfowindow(marker, pname);
            });

            // 마우스를 마커 밖으로 옮기면 발생하는 이벤트
            kakao.maps.event.addListener(marker, 'mouseout', function() {
                simpleinfowindow.close();
            });

            // 리스트의 아이템을 클릭하면 발생하는 이벤트
            itemEl.onclick = function() {
                // 1. 주소 정보들을 text 영역으로 전송 (hidden 사용)

               // 2. 길찾기 Get쿼리 추가 (탐험하기로 통합)

            };

        })(map, marker, places[i].place_name, places[i].road_address_name, places[i].address_name, places[i].y, places[i].x);

        fragment.appendChild(itemEl);

    }

    // 검색결과 항목들을 검색결과 목록 Elemnet에 추가합니다
    listEl.appendChild(fragment);
    menuEl.scrollTop = 0;

    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
    map.setBounds(bounds);



}

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {

    var el = document.createElement('li'),
    itemStr = '<span class="markerbg marker_' + (index+1) + '"></span>' +
                '<div class="info" style="cursor:pointer;">' +
                '   <h5>' + places.place_name + '</h5>';

    if (places.road_address_name) {
        itemStr += '    <span>' + places.road_address_name + '</span>' +
                    '   <span class="jibun gray">' +  places.address_name  + '</span>';
    } else {
        itemStr += '    <span>' +  places.address_name  + '</span>';
    }
      itemStr += '  <span class="tel">' + places.phone + '</span>'; +
                '</div>';

    el.innerHTML = itemStr;
    el.className = 'item';

    return el;
}


// 장소검색으로 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, idx) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
        imgOptions =  {
            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
            marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage
        });

    marker.setMap(map); // 지도 위에 마커를 표출합니다

    markersCategory[1] = markersCategory[1] || [];
    markersCategory[1].push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}

// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarkerCategory(position, order) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(27, 28),  // 마커 이미지의 크기
        imgOptions =  {
            spriteSize : new kakao.maps.Size(72, 208), // 스프라이트 이미지의 크기
            spriteOrigin : new kakao.maps.Point(46, (order*36)), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(11, 28) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
            marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage
        });

    marker.setMap(map); // 지도 위에 마커를 표출합니다

    // markersCategory[order]가 없으면 빈 배열로 초기화
    markersCategory[order] = markersCategory[order] || [];
    markersCategory[order].push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
    for ( var i = 0; i < markers.length; i++ ) {
        markers[i].setMap(null);
    }
    markers = [];
}

function removeMarkerCategory(order) {
    if (markersCategory[order]) {
        for (var i = 0; i < markersCategory[order].length; i++) {
            markersCategory[order][i].setMap(null);
        }
    }
}


// 검색을 통해 배열에 추가된 마커들을 지도에 표시하거나 삭제하는 함수입니다
function setSearchMarkers(map) {
    if (markersCategory[1]) {
        for (var i = 0; i < markersCategory[1].length; i++) {
            markersCategory[1][i].setMap(map);
        }
    }
}

// "마커 보이기" 버튼을 클릭하면 호출되어 배열에 추가된 마커를 지도에 표시하는 함수입니다. (검색버튼을 눌렀을때, 검색배열만)
function showMarkers(orderNumber) {
    if (orderNumber === 1) {
        setSearchMarkers(map);
    }
}

// "마커 감추기" 버튼을 클릭하면 호출되어 배열에 추가된 마커를 지도에서 삭제하는 함수입니다 (현재 선언은 했으나 미사용)
function hideMarkers() {
    setSearchMarkers(null);
}

// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(pagination) {
    var paginationEl = document.getElementById('pagination'),
        fragment = document.createDocumentFragment(),
        i;

    // 기존에 추가된 페이지번호를 삭제합니다
    while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild (paginationEl.lastChild);
    }

    for (i=1; i<=pagination.last; i++) {
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = i;

        if (i===pagination.current) {
            el.className = 'on';
        } else {
            el.onclick = (function(i) {
                return function() {
                    pagination.gotoPage(i);
                }
            })(i);
        }

        fragment.appendChild(el);
    }
    paginationEl.appendChild(fragment);
}

function displaysimpleInfowindow(marker, title) {
    var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';
    simpleinfowindow.setContent(content);
    simpleinfowindow.open(map, marker);
}

// 마커를 클릭했을 때 호출되는 함수입니다
function displayInfowindow(marker, pname, praddress, paddress) {
     var content = '<div class = "wrap">' +
                '		       <div class = "classimg"><img src = "/MapSearch/samplelogo.jpg" width="160" height="160"></div>' +
                         '        <div class="classinfo">' +
                            '            <div class="classtitle">' + pname + '</div>' +
                            '            <div class="classfounder">' + paddress + '</div>' +
                            '            <div class="classbutton">' +
                            '            <button type = "button" id = "selectstarting"> 출발지 </button>' +
                            '            <button type = "button" id = "selectdestination"> 도착지 </button>' +
                            '           </div>' +
                         '        </div>' +
                '			   </div>';
    infowindow.setContent(content);
    infowindow.open(map, marker);
}

 // 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) {
    while (el.hasChildNodes()) {
        el.removeChild (el.lastChild);
    }
}

// 각 카테고리에 클릭 이벤트를 등록합니다
function addCategoryClickEvent() {
    var category = document.getElementById('category'),
        children = category.children;

    for (var i=0; i<children.length; i++) {
        children[i].onclick = onClickCategory;
    }
}

function onClickCategory() {
    var id = this.id,
    className = this.className;
    orderNumber = parseInt(this.getAttribute('data-order'));

    if (className === 'on') {
        currCategory = '';
//        changeCategoryClass();
        removeMarkerCategory(orderNumber);
        removeMarker();
    } else {
        currCategory = id;
//        changeCategoryClass(this);
//        recoverMarkerCategory();
        mySearchPlaces();
        showMarkers(orderNumber);
    }
     toggleCategoryClass(this);
}

// 클릭된 카테고리에 스타일을 토글하는 함수입니다
function toggleCategoryClass(el) {
    if (el) {
        el.classList.toggle('on');
    }
}

//// 클릭된 카테고리에만 클릭된 스타일을 적용하는 함수입니다
//function changeCategoryClass(el) {
//    var category = document.getElementById('category'),
//        children = category.children,
//        i;
//
//    for ( i=0; i<children.length; i++ ) {
//        children[i].className = '';
//    }
//
//    if (el) {
//        el.className = 'on';
//    }
//}