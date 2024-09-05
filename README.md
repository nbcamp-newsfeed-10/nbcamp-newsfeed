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

### Post

- 게시글 작성 (Create Post)
	- method: `POST`
	- URL: `/posts`
	- 설명: 게시글을 작성합니다.
	- Request Body:    
    ```
    {
      "title": "string",
      "content": "string"
    }
    ```
	- Response:
    ```
    {
      "isSuccess": true,
      "code": "200",
      "message": "Ok",
      "result": {
        "postId": "long",
        "userId": "long",
        "title": "string",
        "content": "string",
        "createdAt": "string",
        "updatedAt": "string"
      }
    }
    ```   

- 게시글 조회 (Get Post)
	- method: `GET`
	- URL: `/posts/{postId}`
	- 설명: 특정 게시글을 조회합니다.
	- Response: 
    ```
    {
      "isSuccess": true,
      "code": "200",
      "message": "Ok",
      "result": {
        "postId": "long",
        "userId": "long",
        "title": "string",
        "content": "string",
        "createdAt": "string",
        "updatedAt": "string"
      }
    }    
    ```

- 게시글 수정 (Update Post)
	- method: `PUT`
	- URL: `/posts/{postId}`
	- 설명: 특정 게시글을 수정합니다.
	- Request Body:
    ```
    {
      "title": "string",
      "content": "string"
    }
    ```
	- Response:
    ```
    {
      "isSuccess": true,
      "code": "200",
      "message": "Ok",
      "result": {
        "postId": "long",
        "userId": "long",
        "title": "string",
        "content": "string",
        "createdAt": "string",
        "updatedAt": "string"
      }
    }    
    ```

- 게시글 삭제 (Delete Post)
	- method: `DELETE`
	- URL: `/posts/{postId}`
	- 설명: 특정 게시글을 삭제합니다.
	- Response:
    ```
    {
      "isSuccess": true,
      "code": "200",
      "message": "Ok",
      "result": "Post Delete Success"
    }    
    ```
    
- 뉴스피스 조회 (Get Newsfeed)
	- method: `GET`
	- URL: `/posts/newsfeed`
	- 설명: 각 페이지 당 뉴스피드 데이터를 10개씩 조회합니다.
	- Response:
    ```
    {
      "isSuccess": true,
      "code": "200",
      "message": "Ok",
      "result": {
        "postId": "long",
        "userId": "long",
        "title": "string",
        "content": "string",
        "createdAt": "string",
        "updatedAt": "string"
      }
    }
    ```
### Comment
- 댓글 작성 (Create Comment)
	- method: `POST`
	- URL: `/posts/{postId}/comments`
	- 설명: 특정 게시글에 댓글을 작성합니다.
	- Request Body:
    ```
    {
      "postId" : "long",
      "userId": "long",
      "content": "string"
    }
    ```
	- Response:
    ```
    {
      "isSuccess": true,
      "code": "200",
      "message": "Ok",
      "result": {
        "commentId": "long",
        "postId": "long",
        "userId": "long",
        "content": "string",
        "createdAt": "string"
      }
    }
    ```
    
- 댓글 수정 (Update Comment)
	- method: `PUT`
	- URL: `/comments/{commentId}`
	- 설명: 특정 댓글을 수정합니다.
	- Request Body:   
    ```
    {
        "postId" : "long",
        "userId" : "long",
        "content" : "string"
    }
    ```    
	- Response: 
    ```
    {
      "isSuccess": true,
      "code": "200",
      "message": "Ok",
      "result": {
        "commentId": "long",
        "content": "string",
        "updatedAt": "string"
      }
    }   
    ```
    
- 댓글 삭제 (Delete Comment)
	- method: `DELETE`
	- URL: `/comments/{commentId}`
	- 설명: 특정 댓글을 삭제합니다.
	- Response:
    ```
    {
      "isSuccess": true,
      "code": "200",
      "message": "Ok",
      "result": "Comment Delete Success"
    }
    ```
    
- 특정 게시글에 대한 댓글 전체 조회 (Get Comments)
	- method: `GET`
	- URL: `/comments/posts/{postId}`
	- 설명: 특정 게시글에 대한 댓글을 전체 조회합니다.
	- Response:  
    ```
    {
      "isSuccess": true,
      "code": "200",
      "message": "Ok",
      "result": [
        {"commentId": "long",
        "content": "string",
        "userId": "long"}
      ]
    }
    ```
    
- 특정 게시글 댓글 개수 조회 (Get Comment Count)
	- method: `GET`
	- URL: `/comments/{postId}/count`
	- 설명: 특정 게시글에 대한 댓글의 개수를 조회합니다.
	- Response:    
    ```
    {
      "isSuccess": true,
      "code": "200",
      "message": "Ok",
      "result": {
        "postId": "long"
        "commentCount": "long"
      }
    }  
    ```

### Like
- 좋아요 추가 (Add Like)
	- method: `POST`
	- URL: `/posts/{postId}/like`
	- 설명: 특정 게시글에 좋아요를 추가합니다.
	- Response:
    ```
    {
      "isSuccess": true,
      "code": "200",
      "message": "Ok",
      "result": {
        "postId": "long",
        "userId": "long"
      }
    }
    ```

- 좋아요 취소 (Remove Like)
	- method: `DELETE`
	- URL: `/posts/{postId}/like`
	- 설명: 특정 게시글에 추가한 좋아요를 취소합니다.
	- Response: 
    ```
    {
      "isSuccess": true,
      "code": "200",
      "message": "Ok",
      "result": "Unlike Post Success"
    }
    ```
    
- 특정 게시글 좋아요 개수 조회 (Get Like Count)
	- mehtod: `GET`
	- URL: `/posts/{postId}/like/count`
	- 설명: 특정 게시글에 대한 좋아요의 개수를 조회합니다.
	- Response:  
    ```
    {
      "isSuccess": true,
      "code": "200",
      "message": "Ok",
      "result": {
        "postId": "long"
        "likeCount": "long"
      }
    }
    ```
    
## ERD
![image](https://github.com/user-attachments/assets/57449240-ec83-40a3-9876-ed4c0fbe5bbd)
