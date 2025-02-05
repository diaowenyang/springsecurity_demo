# SpringSecurityDemo

---

### 项目技术架构概述

    目前基于Java 17，Spring boot 3.x开发;  

---

### 规范说明

+ 推荐使用google-java-format插件，用于做统一代码格式化
+ 基于SpringSecurity进行登录鉴权校验
+ 登录时返回 access_token 、refresh_token
+ 用户访问受限资源时携带access_token访问
+ 基于SpringSecurity表达式驱动的权限控制

---     

### 权限管理

+ 用户登录时把用户的权限加载到SpringSecurity的上下文
+ 用户访问接口的时候，SpringSecurity进行判断用户是否有该接口的访问权限

---

### 接口

1.登录接口
curl --location 'http://127.0.0.1:8080/api/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
"email":"test@a.com",
"code":"123456"
}'

返回：
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJVU0VSX0lEIjoxLCJ0eXBlIjoiQUNDRVNTIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6ImFhIn0seyJhdXRob3JpdHkiOiJiYiJ9XSwic3ViIjoid2VuQGdtYWlsLmNvbSIsImlhdCI6MTczODcxNjU1NSwiZXhwIjoxNzM5MzIxMzU1fQ.x3qfv6gGrni-85wXObGK8_zBtTRNO-2TZFXKA-fDkos",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJVU0VSX0lEIjoxLCJ0eXBlIjoiUkVGUkVTSCIsInN1YiI6IndlbkBnbWFpbC5jb20iLCJpYXQiOjE3Mzg3MTY1NTUsImV4cCI6MTc0MTMwODU1NX0.FSfFehXpzKrIHazaKz1t0kyVz15iMtVqlllZMh823d4"
}
```

2.刷新token接口
curl --location 'http://127.0.0.1:8080/api/auth/refresh?refreshToken=eyJhbGciOiJIUzI1NiJ9.eyJVU0VSX0lEIjoxLCJ0eXBlIjoiUkVGUkVTSCIsInN1YiI6IndlbkBnbWFpbC5jb20iLCJpYXQiOjE3Mzg3MTY1NTUsImV4cCI6MTc0MTMwODU1NX0.FSfFehXpzKrIHazaKz1t0kyVz15iMtVqlllZMh823d4'

返回：
{
"accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJVU0VSX0lEIjoxLCJ0eXBlIjoiQUNDRVNTIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6ImFhIn0seyJhdXRob3JpdHkiOiJiYiJ9XSwic3ViIjoid2VuQGdtYWlsLmNvbSIsImlhdCI6MTczODcxNjY2NiwiZXhwIjoxNzM5MzIxNDY2fQ.NbNUlZiIaB5YPYsifOIuO3iuHPmumJJdpBkjD-LkMUo",
"refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJVU0VSX0lEIjoxLCJ0eXBlIjoiUkVGUkVTSCIsInN1YiI6IndlbkBnbWFpbC5jb20iLCJpYXQiOjE3Mzg3MTY2NjYsImV4cCI6MTc0MTMwODY2Nn0.nnrnfxyl-s6m1T2nP6YdormIQ3WDUNv180c5Bqr5768"
}

3.携带token访问受限资源
curl --location 'http://127.0.0.1:8080/api/test/protected/uri1' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJVU0VSX0lEIjoxLCJ0eXBlIjoiQUNDRVNTIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6ImFhIn0seyJhdXRob3JpdHkiOiJiYiJ9XSwic3ViIjoid2VuQGdtYWlsLmNvbSIsImlhdCI6MTczODcxNjY2NiwiZXhwIjoxNzM5MzIxNDY2fQ.NbNUlZiIaB5YPYsifOIuO3iuHPmumJJdpBkjD-LkMUo'

返回：
状态码200，返回内容「测试是否有权访问」

curl --location 'http://127.0.0.1:8080/api/test/protected/uri2' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJVU0VSX0lEIjoxLCJ0eXBlIjoiQUNDRVNTIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6ImFhIn0seyJhdXRob3JpdHkiOiJiYiJ9XSwic3ViIjoid2VuQGdtYWlsLmNvbSIsImlhdCI6MTczODcxNjY2NiwiZXhwIjoxNzM5MzIxNDY2fQ.NbNUlZiIaB5YPYsifOIuO3iuHPmumJJdpBkjD-LkMUo'

返回：
状态码403