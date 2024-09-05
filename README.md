# 🖥 Newsfeed Project
Spring Boot를 활용해 친구들의 최신 게시물을 볼 수 있는 뉴스피드의 서버입니다.


## ⏲ 개발기간
2024/08/30 ~ 2024/09/05

## 🧑‍💻 멤버구성 및 역할분담
- 팀원 강이원: 프로필 관리 구현

- 팀원 김기혜: 뉴스피드 게시물 관리 구현

- 팀원 배주희: 좋아요 구현

- 팀장 손승진: 댓글 구현

- 팀원 황인서: 사용자 인증, 친구 관리 구현

## 📱 개발 환경
- **Framework**: Spring Boot, JPA

- **Database**: MySQL

## 📚 주요 기능
- 회원 가입, 로그인
- 게시물 작성, 조회, 수정, 삭제
- 댓글 작성, 조회, 수정, 삭제
- 좋아요 생성, 조회, 삭제
- 친구 요청, 수락, 삭제
- 뉴스피드 조회

## 🔗 와이어 프레임
- [피그마 와이어프레임](https://www.figma.com/design/ojQlzmDOcEPbpL6xqw75Sw/%EB%89%B4%EC%8A%A4%ED%94%BC%EB%93%9C?t=mjPdfnULERRtgdII-0)

## 📜 API 명세

### USER
- 회원가입 기능
  - method: `post`
  - uri: `/auth/signup`
  - request header:
  - response header: `"Authorization": "Bearer xxxx"`
  - query params:
  - request body:
    ```
    {
    "username" : string,
    "email" : string,
    "bio" : string,
    "password" : string
    }
    ```
  - response body:
    ```
    {
    "isSuccess": true,
    "code": "200",
    "message": "Ok",
    "result":
      {
      "userId": int,
      "username": string,
      "email": string,
      "bio": string,
      "createdAt": string,
      "updatedAt": string
      }
    }
    ```

- 로그인 기능
  - method: `post`
  - uri: `/auth/signin`
  - request header:
  - response header: `"Authorization": "Bearer xxxx"`
  - query params:
  - request body:
    ```
    {
    "email" : string,
    "password" : string
    }
    ```
  - response body:
    ```
    {
    "isSuccess": true,
    "code": "200",
    "message": "Ok",
    "result": "Login Success"
    }
    ```

- 프로필 조회 기능
  - method: `get`
  - uri: `/users/{userId}/profile`
  - request header: `"Authorization": "Bearer xxxx"`
  - response header:
  - query params:
  - request body:
  - response body:
    (my profile)
    ```
    {
    "isSuccess": true,
    "code": "200",
    "message": "Ok",
    "result": {
      "userId": int,
      "username": string,
      "email": string,
      "bio": string,
      "active": true,
      "createdAt": string,
      "updatedAt": string
      }
    }
    ```
    (other profile)
    ```
    {
    "isSuccess": true,
    "code": "200",
    "message": "Ok",
    "result": {
      "userId": int,
      "username": string,
      "bio": string,
      "active": true
      }
    }
    ```
    
- 프로필 수정 기능
  - method: `put`
  - uri: `/users/{userId}/profile`
  - request header: `"Authorization": "Bearer xxxx"`
  - response header:
  - query params:
  - request body:
    ```
    {
    "username" : string,
    "bio" : string,
    "currentPassword" : string,
    "newPassword" : string
    }
    ```
  - response body:
    ```
    {
    "isSuccess": true,
    "code": "200",
    "message": "Ok",
    "result": {
      "userId": int,
      "username": string,
      "bio": string,
      "email": string,
      "active": true,
      "updatedAt": string
      }
    }
    ```

- 회원 탈퇴 기능
  - method: `delete`
  - uri: `/users/{userId}/profile`
  - request header: `"Authorization": "Bearer xxxx"`
  - response header:
  - query params:
  - request body:
    ```
    {
    "email" : string,
    "password" : string
    }
    ```
  - response body:
    ```
    {
    "isSuccess": true,
    "code": "200",
    "message": "Ok",
    "result": "Withdraw Success"
    }
    ```

### FRIEND
- 친구 신청 기능
  - method: `post`
  - uri: `/friends/{userId}`
  - request header: `"Authorization": "Bearer xxxx"`
  - response header:
  - query params:
  - request body:
  - response body:
    ```
  	{
  	"isSuccess": true,
  	"code": "200",
  	"message": "Ok",
  	"result": "Friend Request Success"
  	}
    ```
	
- 친구 수락 기능
  - method: `post`
  - uri: `/friends/{userId}/accept`
  - request header: `"Authorization": "Bearer xxxx"`
  - response header:
  - query params:
  - request body:
  - response body:
    ```
  	{
  	"isSuccess": true,
  	"code": "200",
  	"message": "Ok",
  	"result": "Friend Request Accept Success"
  	}
    ```

- 친구 삭제 기능
  - method: `delete`
  - uri: `/friends/{userId}`
  - request header: `"Authorization": "Bearer xxxx"`
  - response header:
  - query params:
  - request body:
  - response body:
    ```
  	{
  	"isSuccess": true,
  	"code": "200",
  	"message": "Ok",
  	"result": "Friend Delete Success"
  	}
    ```
