# 2. 서블릿

<br/>

## HTTP 요청 데이터

<br/>

### Request


- **Start Line**
    > - HTTP 메소드  
  > - URL  
  > - 쿼리 스트링  
  > - 스키마, 프로토콜
- **Header**
- **Body**
    > - form 파라미터 형식 조회  
  > - message body 데이터 직접 조회

<br/>

### GET - Query Parameter
- 메세지 바디 없이 URL의 쿼리 파라미터에 데이터를 포함
- request.getParameter() 사용 가능
- ex) 검색, 필터, 페이징 등에서 많이 사용

<br/>

### POST - HTML Form
- html form에서 submit하고 전송!
- HTTP message body에 쿼리 파라미터 형식으로 넘어감
- request.getParameter() 사용 가능
- Content-Type: application/x-www-form-urlencoded
- ex) 회원가입, 상품 주문

<br/>

### HTTP message body
- HTTP API에서 주로 사용
- 대부분 JSON 형식을 쓴다!! (그 외에서 XML, TEXT 등)
- 받아온 data를 객체로 변환해주어야 함
  1. request 객체에서 ServletInputStream 객체를 받아옴
  2. StreamUtils.copyToString() 메소드를 사용해 ServletInputStream을 String으로 변환
  3. ObjectMapper를 이용하여 data(ex. JSON)를 객체로 변환
- POST, PUT, PATCH에서 주로 사용

<br/>

## HTTP 응답 데이터

<br/>

### 응답 메세지 생성
> - HTTP 응답코드 지정
> - Header 생성
> - Body 생성

<br/>

### 편의 기능
> - Content-Type
> - Cookie
> - Redirect

<br/>

### 단순 텍스트, HTML
- response.getWriter()
- print(), println() 등의 메소드를 사용하여 전달

<br/>

### API JSON
- ObjectMapper의 writeValuesAsString() 메소드 사용
- response.getWriter()
- print(), println() 등의 메소드를 사용하여 전달

<br/>
<br/>