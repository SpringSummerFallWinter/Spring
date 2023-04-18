# 3. 서블릿, JSP, MVC 패턴

<br/>

## 서블릿
- PrintWriter로 HTML 출력
- 으악!

<br/>

## JSP
- HTML 코드 안에 비즈니스 로직 -> 조금 더 깔끔해짐
- 뷰 혹은 비즈니스 로직 중 하나만 수정해도 JSP 전체를 수정해야 함
- 변경 주기가 다르다면 분리하는 것이 좋다.

<br/>

## MVC 패턴
- Model, View, Controller로 분리
- 클라이언트 요청 -> Controller -> 비즈니스 로직 -> Model에 데이터 담아 전달 -> View
- 각자의 역할에만 충실해짐
- JSP에서 제공하는 기능 + jstl을 이용하면 매우 코드가 깔끔해짐
- **한계**
  - 중복되는 로직이 존재
  - 공통 처리 어려움
  - 다음: Front Controller (수문장 역할), 컨트롤러 호출 전에 공통 기능을 처리함

<br/>
<br/>