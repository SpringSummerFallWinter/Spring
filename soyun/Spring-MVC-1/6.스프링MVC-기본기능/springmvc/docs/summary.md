# 6. 스프링 MVC - 기본 기능

<br/>

## 개요
- HTTP 요청/응답 시에 사용할 수 있는 스프링의 다양한 기능을 학습했다.
- 스프링이 제공하는 로깅 기능에 대하여 간단히 알아보았다.

<br/>

## 로깅 기능 간단히 알아보기
<br/>

### `System.out.prinln()` 대신 로깅 라이브러리를 사용하는 이유
- **개발서버**에서는 디버깅을 위해 상세히 로그를 출력하고 **운영서버**에서는 깔끔하게 로그를 출력하고 싶지 않을 수 있다.
- `System.out.prinln()`으로 로그를 출력했다면, 운영서버에서는 이를 모두 주석처리 해주어야 한다!! -> 귀찮다.
- 로깅 라이브러리는 **로그 레벨을 두어 상황에 맞게 로그를 출력**할 수 있는 기능을 제공한다.
- +) 콘솔 뿐만 아니라, 파일이나 네트워크 등 다양한 위치에 로그를 남길 수 있다.
- ++) 성능도 로깅 라이브러리가 훨씬 뛰어나다.


<br/>

- **결론) 반드시 로깅 라이브러리를 사용하도록 하자.**

<br/>

### 로그 라이브러리
- 인터페이스: `SLF4J`
- 구현: `Logback`

<br/>

### 로그 레벨
- `TRACE > DEBUG > INFO > WARN > ERROR`
- `application.properties`에 패키지의 로그 레벨을 특정할 수 있다.
  - `logging.level.hello.springmvc=info`: `hello.springmvc` 패키지에서는 `INFO` 레벨 까지의 로그만 보이겠다!

<br/>

## HTTP 요청
<br/>

### 요청 메서드 매핑
- `@RequestMapping` 어노테이션 사용
- `@(Get, Post, Put, Delete)Mapping`등 축약 형태의 어노테이션 사용
- Path Variable 사용 (RequestParam 방식과는 다르다!!)
  - 최근 HTTP API가 선호하는 방식
  - URL 경로 템플릿화 + `@PathVariable`
      ```java
        /**
          * PathVariable 사용
          * 변수명이 같으면 생략 가능
          * @PathVariable("userId") String userId -> @PathVariable userId 
        */
        @GetMapping("/mapping/{userId}")
        public String mappingPath(@PathVariable("userId") String data) {
          log.info("mappingPath userId={}", data);
          return "ok";
        }
      ```
- 특정 파라미터 조건 매핑
  - `@GetMapping(value = "/mapping-param", params = "mode=debug")`
  - `mode`라는 파라미터의 값이 `debug`일 때에만 매핑 -> `/mapping-param?mode=debug`
- 특정 헤더 조건 매핑
  - `@GetMapping(value = "/mapping-param", headers = "mode=debug")`
- 미디어 타입 조건 매핑
  - `@PostMapping(value = "/mapping-consume", consumes = "application/json")`
  - 헤더의 `Content-Type`이 `application/json`인 요청만 매핑 
  - `@PostMapping(value = "/mapping-produce", produces = "application/json")`
  - 헤더의 `Accept`가 `application/json`인 요청만 매핑

<br/>

### 요청 파라미터 vs 요청 메세지
- 요청 파라미터: HTTP 파라미터로 직접 전달
- 요청 메세지: HTTP message body에 담아서 전달, 메세지 컨버터 필요

### 요청 파라미터 - 쿼리 파라미터, HTML Form
- `@RequestParam`
  - 하나의 파라미터 1:1 바인딩, 파라미터가 단순 타입(String, int, Integer 등)일 때 사용
  - `@RequestParam("username") String username`
  - 혹은 `@RequestParam String username` -> HTTP 파라미터와 매개변수의 이름이 같을 경우에만
  - 혹은 @RequestParam 자체를 생략해도 바인딩 이 된다!! -> **그러나 어노테이션이 있는 게 더 명확하다.**
  - `@RequestParam(required = true/false, defaultValue = "기본값")`
    - `required = true`인데 값이 없다면 400 ERROR 
    - `defaultValue`는 null 뿐만 아니라 **빈 문자열에도 적용**된다!
- `@ModelAttribute`: 여러 파라미터를 1:1로 하나의 객체에 바인딩
  - 유효성 검사를 추가로 해주어야 함! (`BindException` 핸들링)
  - 객체에 `Setter`나 모든 필드를 인자로 받는 생성자가 있어야 바인딩 가능 -> 걍 Lombok 때려박기 

<br/>

### 요청 메세지 - 단순 텍스트, JSON
- `HttpEntity`
  - HTTP header, body 정보 조회 가능
    - `httpEntity.getBody();`, `httpEntity.getHeaders();`
  - 이를 상속한 `RequestEntity` 존재
- `@RequestBody`
  - HTTP body 정보 조회 가능
  - header 정보 조회를 하고 싶다면`@RequestHeader`를 사용하면 된다.
  - 생략 불가능 -> 생략하면 `@RequestParam` 혹은 `@ModelAttribute`로 인식!

<br/>

## HTTP 응답

<br/>

### HTTP API (Message Body에 직접 입력)
- `HttpEntity`
  - HTTP 메세지 바디와 HTTP 응답 코드를 설정하여 반환 가능
    - `return new HttpEntity<>(helloData, HttpStatus.OK);`
  - 이를 상속한 `ResponseEntity` 존재
- `@ResponseBody`
  - HTTP 메세지 바디 반환 가능
    - `return helloData;`
  - `@ResponseStatus(HttpStatus.OK)`와 같이 어노테이션을 추가해주면 응답 코드도 설정 가능
    - 그러나 동적 설정이 불가하기 때문에 필요하다면 `ResponseEntity`를 사용하자

<br/>

### `RestController`
- `Controller` + `ResponseBody` -> 해당 컨트롤러 전체에 `ResponseBody` 적용

<br/>

## HTTP 메세지 컨버터

<br/>

### ArgumentResolver
- 요청 파라미터를 변환하고 처리
- ex) `Model`, `@RequestParam`, `int` 등 어떠한 파라미터라도 값이 딱딱 잘 들어온다. 
- 컨트롤러가 필요로 하는 다양한 파리미터 값(객체)을 생성
- 파라미터가 모두 준비되면 컨트롤러 호출하면서 값 넘겨줌

<br/>

### ReturnValueHandler
- 응답 값을 변환하고 처리
- ex) `ModelAndView`로 반환해도 `String`으로 반환해도 똑같이 뷰를 렌더링한다.

<br/>

### HTTP 메세지 컨버터
- HTTP Message Body에 데이터를 담아서 요청/응답하는 경우 적용 
  - `ArgumentResolver`, `ReturnValueHandler`가 메세지 컨버터를 호출
- 클래스 타입과 미디어 타입(`text/plain`, `application/json` 등)을 체크하여 어떤 메세지 컨버터를 사용할 지 결정

<br/>


<br/>
<br/>