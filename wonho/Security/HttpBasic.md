# HTTP Basic

## HTTP Basic 인증?

HTTP는 자체적인 인증 관련 기능을 제공하며 HTTP 표준에 정의된 가장 단순한 인증 기법이다.

### 특징

- 간단한 설정과 Stateless가 장점이다.

  - Session Cookie(JSESSIONID)를 사용하지 않는다

- 보호자원 접근 시 서버가 클라이언트에게 401 Unauthorized 응답과 함께 WWW-Authenticate header를 기술해서 인증요구를 보냄
- Client는 ID:Password 값을 Base64로 인코딩한 문자열을 Authorization Header에 추가한 뒤 Server에게 Resource를 요청
  - Authorization: Basic cmVzdDpyZXN0
- ID, Password가 Base64로 인코딩되어 있어 ID, Password가 외부에 쉽게 노출되는 구조이기 때문에 SSL이나 TLS는 필수이다.
