# 프로젝트 정보

데이터사이언스(AI)를 활용한 웹서비스 융합 개발자 양성 과정 
B팀 2조 (김용구, 윤지원, 이건구, 장미나, 장찬익)

# 개발 환경

#### Spring Initializr 설정 (의존성 포함)

- Project : Gradle-Groovy
- Language : JAVA
- Spring Boot : 3.1.5
- Package name : com.project.Restaurant
- Packaging : Jar
- JAVA Version : 17

- Dependencies :
  - Spring Boot DevTools
  - Lombok
  - Spring Web
  - Thymeleaf

- Add Dependencies :

  - 타임리프 추가기능 관련
    - implementation 'nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect'
  - MySQL 관련	
    - implementation 'com.mysql:mysql-connector-j:8.1.0'
    - runtimeOnly 'org.postgresql:postgresql'
  - JPA 관련	
    - implementation 'jakarta.persistence:jakarta.persistence-api:3.1.0'
    - implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
  - 입력값 검증 관련
    - implementation 'org.springframework.boot:spring-boot-starter-validation'
  - 스프링 시큐리티 관련 (스프링부트 프레임워크 + 타임리프 시큐리티 관련)
    - implementation 'org.springframework.boot:spring-boot-starter-security'
    - implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.1.RELEASE'
   
# 개발 규칙

- 아래 모든 사항은 권장사항임
- 카멜표기법(wordSample)과 파스칼표기법(WordSample)을 적절히 사용
- 클래스명은 파스칼표기법으로 표기
- 도메인 경로 규칙 (Request, Get, Post 모두 해당)
  - 클래스별 메인페이지 : /place (클래스명)
  - 클래스별 기능페이지 : /place/search, /place/regist (클래스명/기능)
- 그 외에는 직관적으로 변수의 의미를 유추할 수 있도록 작성 (유지보수 및 가독성 향상)

# ER 설계 및 DB 변수

#### Member(회원)


#### Place(맛집장소)


#### PlaceInfo(맛집상세정보)


#### Reservation(예약정보)


#### Board(커뮤니티)






