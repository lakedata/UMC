# CRUD API 
> UMC-DONGDUK 4th Server-Spring Mission
<br>

## 🛠 Tech Stack
<img src="https://img.shields.io/badge/intellijidea-000000?style=for-the-badge&logo=intellijidea&logoColor=white"> <img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonawsa&logoColor=white"> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white">  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"> 
<br>
<br>

## 📌 Mission List
|Week|Topic|Code|
|:--:|:--:|:----:|
|Week6|Backend Language||
|Week7|Restful, Framework||
|Week8|Client Intro||
<br>

Spring Boot Template
- 고객 - Client서버에 요청
- 키오스크 - RouteRestful API를 통해 요청 받음HTTP method, 주소
- 점원 - ControllerPath variable, query string, body 처리json 형식으로 response 반환
- 요리사 - Provider / ServiceProvider - select / Service - insert, update값 가공, 트랜잭션
- 냉장고 - DAO실질적 query 작성, 실행데이터 결과값

Validation
- 형식적 validation: 빈 값 길이, 정규표현식 검사 (부적절한 타입 검사)Controller에 위치
- 논리적 validation: 중복 검사Provider, Service에 위치
<br>

## 💻 Project Structure
```
.
├── HELP.md
├── README.md
├── build
│   ├── classes
│   │   └── java
│   │       └── main
│   │           └── umc
│   │               └── crud
│   │                   ├── CrudApplication.class
│   │                   ├── config
│   │                   │   ├── BaseException.class
│   │                   │   ├── BaseResponse.class
│   │                   │   └── BaseResponseStatus.class
│   │                   └── src
│   │                       ├── TestController.class
│   │                       └── board
│   │                           ├── BoardController.class
│   │                           ├── BoardDao.class
│   │                           ├── BoardService.class
│   │                           └── model
│   │                               ├── BoardDto.class
│   │                               ├── DeleteBoardReq.class
│   │                               ├── DeleteBoardRes.class
│   │                               ├── GetBoardReq.class
│   │                               ├── GetBoardRes.class
│   │                               ├── PostBoardReq.class
│   │                               ├── PostBoardRes.class
│   │                               ├── PutBoardReq.class
│   │                               └── PutBoardRes.class
│   ├── generated
│   │   └── sources
│   │       ├── annotationProcessor
│   │       │   └── java
│   │       │       └── main
│   │       └── headers
│   │           └── java
│   │               └── main
│   ├── resources
│   │   └── main
│   │       ├── application.yml
│   │       ├── static
│   │       └── templates
│   └── tmp
│       └── compileJava
│           └── previous-compilation-data.bin
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
├── src
    ├── main
    │   ├── java
    │   │   └── umc
    │   │       └── crud
    │   │           ├── CrudApplication.java
    │   │           ├── config
    │   │           │   ├── BaseException.java
    │   │           │   ├── BaseResponse.java
    │   │           │   ├── BaseResponseStatus.java
    │   │           │   └── secret
    │   │           │       └── Secret.java
    │   │           ├── src
    │   │           │   ├── board
    │   │           │   │   ├── BoardController.java
    │   │           │   │   ├── BoardDao.java
    │   │           │   │   ├── BoardProvider.java
    │   │           │   │   ├── BoardService.java
    │   │           │   │   └── model
    │   │           │   │       ├── Board.java
    │   │           │   │       ├── GetBoardReq.java
    │   │           │   │       ├── GetBoardRes.java
    │   │           │   │       ├── PostBoardReq.java
    │   │           │   │       ├── PostBoardRes.java
    │   │           │   │       ├── PutBoardReq.java
    │   │           │   │       └── PutBoardRes.java
    │   │           │   ├── test
    │   │           │   │   └── TestController.java
    │   │           │   └── user
    │   │           │       ├── UserController.java
    │   │           │       ├── UserDao.java
    │   │           │       ├── UserProvider.java
    │   │           │       ├── UserService.java
    │   │           │       └── model
    │   │           │           ├── GetUserRes.java
    │   │           │           ├── PatchUserReq.java
    │   │           │           ├── PostLoginReq.java
    │   │           │           ├── PostLoginRes.java
    │   │           │           ├── PostUserReq.java
    │   │           │           ├── PostUserRes.java
    │   │           │           └── User.java
    │   │           └── utils
    │   │               ├── AES128.java
    │   │               ├── JwtService.java
    │   │               └── ValidationRegex.java
    │   └── resources
    │       ├── application.yml
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── umc
                └── crud
                    └── CrudApplicationTests.java
```


