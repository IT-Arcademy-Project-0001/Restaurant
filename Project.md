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
