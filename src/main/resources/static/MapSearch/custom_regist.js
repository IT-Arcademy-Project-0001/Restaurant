////////////////////////////////////////////////  함수 구현  ////////////////////////////////////////////////
 var modalOpen = localStorage.getItem('modalOpen');

 function registPlace(formClass) {
    var Name = document.getElementById('locationName').value;
    var Address = document.getElementById('locationAddress').value;
    var form = document.querySelector('#targetCreateForm');

    if (!Name.trim()) {
         alert('장소이름을 입력해주세요!');
         return false;
    }
    if (!Address.trim()) {
         alert('위치를 선택해주세요!');
         return false;
    }

    if (form) {
       alert('신규장소 추가 요청이 완료되었습니다');
       form.action = "/place/map/add";
       form.submit();
    } else {
       console.error("Form element is null");
    }
 }

////////////////////////////////////////////////  함수 구현 끝  ////////////////////////////////////////////////

////////////////////////////////////////////////  모달 구현  ///////////////////////////////////////////////////
document.addEventListener('keydown', function (e) {
    var modal = document.getElementById('my_modal');
    if (modal && modal.classList.contains('active') && e.key === 'Enter') {
    e.preventDefault();
    }
});

function modal(id) {
    var zIndex = 9999;
    var modal = document.getElementById('my_modal');
    var bg = document.createElement('div');

    bg.setStyle({
        position: 'fixed',
        zIndex: zIndex,
        left: '0px',
        top: '0px',
        width: '100%',
        height: '100%',
        overflow: 'auto',
        backgroundColor: 'rgba(0,0,0,0.4)'
    });

    document.body.append(bg);

    modal.setStyle({
        position: 'fixed',
        display: 'block',
        boxShadow: '0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)',
        zIndex: zIndex + 1,
    });

    // 모달이 열릴 때 내용을 저장
    originalModalContent = modal.innerHTML;

    modal.querySelector('.modal_close_btn').addEventListener('click', function() {
        bg.remove();
        modal.innerHTML = originalModalContent;
        modal.style.display = 'none';
        modalOpen = false;
        localStorage.setItem('modalOpen', modalOpen);
    });
}

Element.prototype.setStyle = function(styles) {
    for (var k in styles) this.style[k] = styles[k];
    return this;
};

document.getElementById('popup_open_btn').addEventListener('click', function() {
    modal('my_modal');
    map_modal_create();
    modalOpen = true;
    localStorage.setItem('modalOpen', modalOpen);
});

////////////////////////////////////////////////  모달 구현 끝  ////////////////////////////////////////////////


//////////////////////////////////////////////// 모달 지도 구현 ////////////////////////////////////////////////


function map_modal_create(){

    var defaultLat = 36.353720; // 기본 위도 값
    var defaultLng = 127.341445; // 기본 경도 값

    var mapContainer_modal = document.getElementById('map_modal'), // 지도를 표시할 div
        mapOption_modal = {
            center: new kakao.maps.LatLng(defaultLat, defaultLng), // 지도의 중심좌표
            level: 5 // 지도의 확대 레벨
        };

    // HTML5의 geolocation으로 사용할 수 있는지 확인합니다
    if (navigator.geolocation) {
        // GeoLocation을 이용해서 접속 위치를 얻어옵니다
        navigator.geolocation.getCurrentPosition(function(position) {

            var lat = position.coords.latitude, // 위도
                lon = position.coords.longitude; // 경도

            var locPosition = new kakao.maps.LatLng(lat, lon); // geolocation으로 얻어온 좌표
            map_modal.setCenter(locPosition);

            // 마커와 인포윈도우를 표시합니다
            currentDisplayMarker2(locPosition);

          }, function(error) {

          // GeoLocation 허용하지 않았을 때.
                alert('GPS 기능이 활성화되지 않았습니다.');
                var locPosition = new kakao.maps.LatLng(36.353720, 127.341445) // 임의 위치의 지도의 중심좌표 (현재 유성온천역, 향후 서울역)
                map_modal.setCenter(locPosition);

          });
    }

    // 현재 GPS상 위치표시용 마커
    function currentDisplayMarker2(locPosition) {

        // 커스텀 마커 이미지 적용
        var imageSrc = '/MapSearch/yourlocation.png', // 마커이미지의 주소입니다
        imageSize = new kakao.maps.Size(49, 59), // 마커이미지의 크기입니다
        imageOption = {offset: new kakao.maps.Point(27, 69)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

        // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption)

        // 마커를 생성합니다
        var marker = new kakao.maps.Marker({
            map: map_modal,
            position: locPosition,
            image: markerImage // 마커이미지 설정
        });

        // 지도 중심좌표를 접속위치로 변경합니다
        map_modal.setCenter(locPosition);
    }

    // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
    var map_modal = new kakao.maps.Map(mapContainer_modal, mapOption_modal);

    // 주소-좌표 변환 객체를 생성합니다
    var geocoder_modal = new kakao.maps.services.Geocoder();
    var marker_modal = new kakao.maps.Marker(); // 클릭한 위치를 표시할 마커입니다

    infowindow_modal = new kakao.maps.InfoWindow({zindex:1}); // 클릭한 위치에 대한 주소를 표시할 인포윈도우입니다

     // 지도를 클릭했을 때 클릭 위치 좌표에 대한 주소정보를 표시하도록 이벤트를 등록합니다
    kakao.maps.event.addListener(map_modal, 'click', function(mouseEvent) {
         searchDetailAddrFromCoords(mouseEvent.latLng, function(result, status) {
             if (status === kakao.maps.services.Status.OK) {
                 var detailAddr = !!result[0].road_address ? '<div>도로명주소 : ' + result[0].road_address.address_name + '</div>' : '';
                 detailAddr += '<div>지번 주소 : ' + result[0].address.address_name + '</div>';

                 var content = '<div class="bAddr">' +
                                 '<span class="title"> 주소정보</span>' +
                                 detailAddr +
                             '</div>';

                 // 마커를 클릭한 위치에 표시합니다
                 marker_modal.setPosition(mouseEvent.latLng);
                 marker_modal.setMap(map_modal);

                 result[0].address.address_name
                 // 인포윈도우에 클릭한 위치에 대한 상세 주소정보를 표시합니다
                 document.getElementById('locationAddress').value = result[0].address.address_name;

                 // 클릭한 위도, 경도 정보를 가져옵니다
                 var latlng = mouseEvent.latLng;

                 console.log(latlng);

                // 클릭한 위도 정보 가져옵니다.
                 var latitude = latlng.getLat();

                 // 클릭한 경도 정보 가져옵니다.
                 var longitude = latlng.getLng();

                 // 화면의 id값이 locationlat에 위도를 넣습니다.
                 document.getElementById('locationLat').value = latitude;

                 // 화면의 id값이 locationlat에 경도를 넣습니다.
                 document.getElementById('locationLng').value = longitude;

             }
         });
     });

     function searchAddrFromCoords(coords, callback) {
         // 좌표로  주소 정보를 요청합니다
         geocoder_modal.coord2RegionCode(coords.getLng(), coords.getLat(), callback);
     }

     function searchDetailAddrFromCoords(coords, callback) {
         // 좌표로  상세 주소 정보를 요청합니다
         geocoder_modal.coord2Address(coords.getLng(), coords.getLat(), callback);
     }

     customerPlaces(map_modal);
 }

 function customerPlaces(map_modal) {

     $.ajax({
         url: '/place/searchModal',
         type: 'GET',
         dataType: 'json',
         data: {
             // 쿼리 문자열로 변환, URL에 포함시켜 서버로 전달.
             latitude2: map.getCenter().getLat(),
             longitude2: map.getCenter().getLng(),
             order2: [2]
         },
         success: function (data) {
             // 서버에서 받아온 데이터를 이용하여 마커 생성
             data.forEach(searchResult3 => {

                 // 마커를 생성하고 지도에 표시합니다
                 var marker = addMarkerCategory2(new kakao.maps.LatLng(searchResult3.locationLat, searchResult3.locationLng), searchResult3.categoryOrder, map_modal);

                 var position = new kakao.maps.LatLng(searchResult3.locationLat, searchResult3.locationLng);

                 var content = '<div class ="labelCustom"><span class="left"></span><span class="center">' + searchResult3.store + '</span><span class="right"></span></div>';


                 var customOverlay = new kakao.maps.CustomOverlay({
                     position: position,
                     content: content,
                     xAnchor: 0.43,
                     yAnchor: 0.91,
                     zIndex:1,
                     clickable: true
                 });

                 customOverlay.setMap(map_modal);

             });
         },
         error: function (error) {
             console.error('Error:', error);
         }
     });
 }

 function addMarkerCategory2(position, order, map_modal) {

     var imageSrc = '/MapSearch/regist_waiting.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
         imageSize = new kakao.maps.Size(30, 30),  // 마커 이미지의 크기
         imgOptions =  {
             offset: new kakao.maps.Point(11, 28) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
         },

         markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
             marker = new kakao.maps.Marker({
             position: position, // 마커의 위치
             image: markerImage
         });

     marker.setMap(map_modal); // 지도 위에 마커를 표출합니다

     return marker;
 }


////////////////////////////////////////////////  모달 지도 구현 끝  ////////////////////////////////////////////////