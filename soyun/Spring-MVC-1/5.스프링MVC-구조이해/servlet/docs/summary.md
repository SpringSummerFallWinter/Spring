# 4. 스프링 MVC - 구조 이해

<br/>

## 개요
- 우리는 FrontController를 직접 구현해보고, 이를 향상시켰다.
- 앞서 구현한 내용들은 모두 Spring Container에서 제공되는 기능이며, 이외에도 더 많은 기능을 제공한다!

<br/>

## 스프링 MVC
<br/>

### 기존과의 비교
- FrontController -> DispatcherServlet
- 핸들러 조회 및 핸들러 매핑 (어떤 핸들러인지?)
- 핸들러 어댑터 목록에서 핸들러 어댑터 조회 (어떤 핸들러 어댑터로 처리할 것인지?)
- 핸들러 어댑터가 실제 핸들러 호출
  - ModelView 반환 -> ModelAndView 반환
- ViewResolver 호출 (Spring의 ViewResolver는 인터페이스로 다양한 ViewResolver 사용 가능)
  - MyView 반환 -> View 반환

<br/>

### 핸들러 매핑
- 1순위
  - RequestMappingHandlerMapping: @Controller, @RequestMapping 어노테이션 기반의 클래스
- 2순위
  - BeanNameUrlHandlerMapping: 스프링 빈 이름 기반

<br/>

### 핸들러 어댑터
- 1순위
  - RequestMappingHandlerAdapter: @Controller, @RequestMapping 어노테이션 기반의 클래스
- 2순위
  - HttpRequestHandlerAdapter: HttpRequestHandler 처리
- 3순위
  - SimpleControllerHandlerAdapter: Controller 인터페이스(어노테이션과 다르다!)

<br/>

### 뷰 리졸버
- `application.properties`에 등록한 `spring.mvc.view.prefix`, `spring.mvc.view.suffix` 정보를 등록해 사용
- InternalViewResolver 클래스를 통해 자바코드로도 prefix, suffix 등록 가능


<br/>

## Spring MVC 적용
<br/>

### V1
- `@Controller` 어노테이션과 `@RequestMapping` 어노테이션 적용
- 여전히 Controller 들은 분리된 상태

<br/>

### V2
- Controller 통합
- 상위 클래스에 `@RequestMapping` 어노테이션을 적용하여 공통적인 url pattern을 분리

<br/>

### V3
- `@GetMapping`, `@PostMapping`
  - GET, POST 등을 분리해야 하는 이유는 역할을 명확하게 하기 위함임
  - 만약 `@GetMapping` 어노테이션을 적용한 메소드를 POST로 호출한다면 405 ERROR가 뜨게 됨 (올바르지 않은 Method)
- `@RequestParam`
  - `request.getParameter()`와 같은 의미
  - 메소드의 파라미터로 바로 받아 올 수 있음
- Model
  - Model 객체에 데이터를 담을 수 있음
  - ModelAndView를 반환하는 대신, String 타입의 View만을 반환

<br/>

<br/>
<br/>