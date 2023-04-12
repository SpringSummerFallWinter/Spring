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
