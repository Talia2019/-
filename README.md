# 공부하는 습관

보상을 통한 동기부여, 활용도 높은 스케줄 관리를 통해 공부하는 습관을 길러주는 화상 스터디 플랫폼 👩‍💻

<br>

## File Structure

- Front

```
├── public
│   ├── icons
│   │   ├── _paper-plane.svg
│   │   ├── _x-btn.svg
│   │   └── success-filled.svg
│   ├── images
│   │   └── login.jpg
│   └── index.html
└── src
    ├── App.js
    ├── components
    │   ├── emailConfirm.js
    │   ├── login.js
    │   └── signup.js
    ├── index.js
    ├── pages
    │   ├── Home.js
    │   ├── Meetingrooms.js
    │   ├── Profile.js
    │   ├── Schedule.js
    │   ├── Settings.js
    │   └── Studyrooms.js
    └── statics
        ├── css
        │   ├── login.css
        │   ├── login.css.map
        │   ├── main.css
        │   ├── main.css.map
        │   ├── modal.css
        │   ├── modal.css.map
        │   ├── signup.css
        │   └── signup.css.map
        └── scss
            ├── login.scss
            ├── main.scss
            ├── modal.scss
            └── signup.scss
```

- Back

```
─── main
    ├── java
    │   └── com.gongsp
    │       │   ServerApplication.java  // 메인 실행 파일
    │       ├── api                     // Backend API 구현 Class 집합
    │       │   ├── controller          // REST API 요청 컨트롤러
    │       │   ├── request             // REST API 요청 DTO
    │       │   ├── response            // REST API 응답 DTO
    │       │   └── service             // 비즈니스 로직처리 서비스
    │       ├── common                  // 공용 Class 집합
    │       │   ├── auth
    │       │   │       JwtAuthenticationFilter.java  // 인증을 위한 JWT 필터
    │       │   │       GongUserDetails.java          // 인증을 위한 UserDetails 구현체
    │       │   │       GongUserDetailService.java    // 인증을 위한 UserDateilsService 구현체
    │       │   ├── model.response
    │       │   │       BaseResponseBody.java         // 공용 Response Body Class
    │       │   └── util
    │       │   │       JwtTokenUtil.java             // JWT 토큰 발급 및 검증 유틸
    │       │   │       ResponseBodyWriteUtil.java    // Response Body 생성 유틸
    │       ├──  config
    │       │       SecurityConfig.java               // Spring Security 설정
    │       └── db
    │           ├── entity
    │           └── repository
    └── resources
            application.properties       // Spring Application 설정 파일
```

<br>

## Members

<table>
  <tr height="307px">
    <td align="center" width="300px">
      <a href="https://github.com/eunyeong1113"><img src="https://avatars.githubusercontent.com/u/59558623?v=4"/></a>
    </td>
     <td align="center" width="300px">
      <a href="https://github.com/rosieyeon/"><img src="https://avatars.githubusercontent.com/u/70363530?v=4"/></a>
    </td>
    <td align="center" width="300px">
      <a href="https://github.com/FallingStar624"><img src="https://avatars.githubusercontent.com/u/83006446?v=4"/></a>
    </td>
  </tr>
  <tr>
    <td align="center" width="300px">
      <strong>은영</strong><br><a href="https://github.com/eunyeong1113">eunyeong1113</a>
    </td>
    <td align="center" width="300px">
      <strong>승은</strong><br><a href="https://github.com/rosieyeon/">rosieyeon</a>
    </td>
    <td align="center" width="300px">
      <strong>광호</strong><br><a href="https://github.com/FallingStar624">FallingStar624</a>
    </td>
  </tr>
</table>

<table>
  <tr>
  <td align="center" width="300px">
      <a href="https://github.com/minchae9"><img src="https://avatars.githubusercontent.com/u/87417226?v=4"/></a>
    </td>
    <td align="center" width="300px">
      <a href="https://github.com/Talia2019"><img src="https://avatars.githubusercontent.com/u/55391944?v=4"/></a>
    </td>
    <td align="center" width="300px">
      <a href="https://github.com/choymoon"><img src="https://avatars.githubusercontent.com/u/27428109?v=4"/></a>
    </td>
     </tr>
  <tr>
<td align="center" width="300px">
      <strong>민채</strong><br><a href="https://github.com/minchae9">minchae9</a>
    </td>
    <td align="center" width="300px">
      <strong>지영</strong><br><a href="https://github.com/Talia2019">Talia2019</a>
    </td>
    <td align="center" width="300px">
      <strong>용문</strong><br><a href="https://github.com/choymoon">choymoon</a>
    </td></tr></table>
