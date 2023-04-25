# 스프링 기본 기능 정리

## 로깅

```java
private final Logger log = LoggerFactory.getLogger(getClass());
String name = "Spring";
log.trace("trace log={}", name);
log.debug("debug log={}", name);
log.info("info log={}", name);
log.warn("warn log={}", name);
log.error("error log={}", name);
```

`logging.level.hello.springmvc=trace`

application.properties 에 이렇게 설정하면 trace, debug, info, warn, error 모두 log 찍어볼 수 있다.

## RequestMapping

```java
@GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data) {
        log.info("data = {}", data);
        return "OK";
    }
```

이런식으로 Parameter 바로 받을 수 있음.

## HTTP 요청 파라미터 - ModelAttribute

```java
public String modelAttribute(@RequestParam String username, @RequestParam int age) {
	HelloData helloData = new HelloData();
	helloData.setUsername(username);
	helloData.setAge(age);
}

public String modelAttribute(@ModelAttribute HelloData helloData) {
	
}
```

위 코드와 아래 코드는 똑같이 동작한다.

마법처럼 HelloData 객체가 생성되고 그안에 값도 알아서 들어간다.

스프링이 하는일

1. HelloData 객체 생성
2. 요청 파라미터 이름으로 HelloData 객체의 프로퍼티를 찾는다. 그리고 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력한다.

그리고

```java
public String modelAttribute(HelloData helloData) {
	
}
```

@ModelAttribute 도 생략가능함. 그런데 @RequestParam도 생략가능해서 혼란스럽다.

String, int , Integer 같은 단순 타입 = RequestParam

나머지는 ModelAttribute (argument resolver 로 지정해둔 타입 외)

## HTTP 요청 메시지 - 단순 텍스트

```java
public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {
	String body = httpEntity.getBody();
	return new HttpEntity<>("ok");
}
```

이렇게 메시지 바디를 받을 수 있고, 또 메시지 바디에 실어 보낼 수 있다.

```java
@ResponseBody
public String requestBodyStringV3(@RequestBody String body) throws IOException {
	return "ok";
}
```

위의 코드는 아래처럼 깔끔하게 변경된다.

**요청 파라미터 vs HTTP 메시지 바디**

- 요청 파라미터 : @RequestParam, @ModelAttribute
- HTTP 메시지 바디: @RequestBody

## HTTP 요청 메시지 - JSON

```java
@ResponseBody
public String requestBodyJsonV3(@RequestBody HelloData helloData) throws IOException {
	return "ok";
}
```

이렇게 하면 JSON 데이터를 바로 받을 수 있다.

```java
@ResponseBody
public HelloData requestBodyJsonV3(@RequestBody HelloData helloData) throws IOException {
	return helloData;
}
```

이렇게하면 helloData 값이 JSON 으로 변환되어서 return 된다.

## 응답

```java
@RequestMapping("/response-view-v1")
public ModelAndView responseViewV1() {
    ModelAndView mav = new ModelAndView("response/hello")
            .addObject("data","hello");
    return mav;
}

@RequestMapping("/response-view-v2")
public String responseViewV2(Model model) {
    model.addAttribute("data", "hhhh");
    return "response/hello";
}
```

이런 방식으로 화면을 보낼 수 있다.

templates 패키지 하위에 response 패키지가 있고 hello.html 파일이 있어야한다.

@RestController = @Controller + @ResponseBody 이다.
