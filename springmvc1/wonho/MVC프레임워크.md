# 프론트 컨트롤러
- 도입 전
![도입 전](./img/FrontControllerPrev.png)

- 도입 후
![도입 후](./img/FrontController.png)

## FrontController 패턴 특징
- FrontController 서블릿 하나로 클라이언트의 요청을 받음
- FrontController가 요청에 맞는 컨트롤러를 찾아서 호출
- 입구를 하나로
- 공통 처리 가능
- 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨

## 스프링 웹 MVC와 프론트 컨트롤러
- 스프링 웹 MVC의 핵심도 바로 FrontController
- 스프링 웹 MVC의 DispatcherServlet이 FrontController 패턴으로 구현되어 있음