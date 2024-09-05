## API 명세

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
