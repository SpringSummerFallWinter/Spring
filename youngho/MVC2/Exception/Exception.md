# Exception

## 

스프링 부트가 기본으로 제공하는 ExceptionResolver 는 다음과 같다.
HandlerExceptionResolverComposite 에 다음 순서로 등록

1. ExceptionHandlerExceptionResolver
2. ResponseStatusExceptionResolver
3. DefaultHandlerExceptionResolver 우선 순위가 가장 낮다.

**ExceptionHandlerExceptionResolver**
@ExceptionHandler 을 처리한다. API 예외 처리는 대부분 이 기능으로 해결한다. 조금 뒤에 자세히
설명한다.

**ResponseStatusExceptionResolver**
HTTP 상태 코드를 지정해준다.
예) @ResponseStatus(value = HttpStatus.NOT_FOUND)

**DefaultHandlerExceptionResolver**
스프링 내부 기본 예외를 처리한다.

# ExceptionHandler

현직에서 api exception에 대부분 사용된다.

```java
@Slf4j
@RestController
public class ApiExceptionV2Controller {
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorResult illegalExceptionHandler(IllegalArgumentException e) {
		return new ErrorResult( "BAD", e.getMessage());
	}
	
	@GetMapping("/api2/members/{id}")
	public MemberDto getMember(@PathVariable("id") String id) {
		
		if(id.equals("ex")) {
			throw new RuntimeException("잘못된 사용자");
		}
		if(id.contentEquals("bad")) {
			throw new IllegalArgumentException("잘못된 입력 값");
		}
		if(id.equals("user-ex")) {
			throw new UserException("사용자 오류");
		}
		
		return new MemberDto(id, "hello " + id);
	}
	

	@Data
	@AllArgsConstructor
	static class MemberDto {
		private String memberId;
		private String name;
	}
}
```

이렇게 되면 완전히 정상적인 흐름으로 흘러가기 때문에 HTTP STATUS가 200으로 반환된다.

메소드 위에

```java
@ResponseStatus(HttpStatus.BAD_REQUEST)
```

이 어노테이션을 하나 붙여준다.
