# Exception

## 체크 예외

체크 예외의 장단점
체크 예외는 예외를 잡아서 처리할 수 없을 때, 예외를 밖으로 던지는 throws 예외 를 필수로 선언해야 한다. 그렇지 않으면 컴파일 오류가 발생한다. 이것 때문에 장점과 단점이 동시에 존재한다.

장점: 개발자가 실수로 예외를 누락하지 않도록 컴파일러를 통해 문제를 잡아주는 훌륭한 안전 장치이다.

단점: 하지만 실제로는 개발자가 모든 체크 예외를 반드시 잡거나 던지도록 처리해야 하기 때문에, 너무 번거로운 일이 된다. 크게 신경쓰고 싶지 않은 예외까지 모두 챙겨야 한다. 추가로 의존관계에 따른 단점도 있는데 이 부분은 뒤에서 설명하겠다.

## 언체크 예외

RuntimeException 과 그 하위 예외는 언체크 예외로 분류된다.
언체크 예외는 말 그대로 컴파일러가 예외를 체크하지 않는다는 뜻이다.
언체크 예외는 체크 예외와 기본적으로 동일하다. 차이가 있다면 예외를 던지는 throws 를 선언하지 않고, 생략할 수 있다. 이 경우 자동으로 예외를 던진다

Unchecked Exception TestCode

```java
package hello.jdbc.exception.basic;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@Slf4j
public class UncheckedTest {
 @Test
 void unchecked_catch() {
 Service service = new Service();
 service.callCatch();
 }
 @Test
 void unchecked_throw() {
 Service service = new Service();
 assertThatThrownBy(() -> service.callThrow())
 .isInstanceOf(MyUncheckedException.class);
 }
 /**
 * RuntimeException을 상속받은 예외는 언체크 예외가 된다.
 */
 static class MyUncheckedException extends RuntimeException {
 public MyUncheckedException(String message) {
 super(message);
 }
 }
 /**
 * UnChecked 예외는
 * 예외를 잡거나, 던지지 않아도 된다.
 * 예외를 잡지 않으면 자동으로 밖으로 던진다.
 */
 static class Service {
 Repository repository = new Repository();
 /**
 * 필요한 경우 예외를 잡아서 처리하면 된다.
 */
 public void callCatch() {
 try {
 repository.call();
 } catch (MyUncheckedException e) {
 //예외 처리 로직
 log.info("예외 처리, message={}", e.getMessage(), e);
 }
 }
 /**
 * 예외를 잡지 않아도 된다. 자연스럽게 상위로 넘어간다.
 * 체크 예외와 다르게 throws 예외 선언을 하지 않아도 된다.
 */
 public void callThrow() {
 repository.call();
 }
 }
 static class Repository {
 public void call() {
 throw new MyUncheckedException("ex");
 }
 }
}
```

djs체크 예외의 장단점
언체크 예외는 예외를 잡아서 처리할 수 없을 때, 예외를 밖으로 던지는 throws 예외 를 생략할 수 있다. 이것 때문에 장점과 단점이 동시에 존재한다.

장점: 신경쓰고 싶지 않은 언체크 예외를 무시할 수 있다. 체크 예외의 경우 처리할 수 없는 예외를 밖으로 던지려면 항상 throws 예외 를 선언해야 하지만, 언체크 예외는 이 부분을 생략할 수 있다. 이후에 설명하겠지만, 신경쓰고 싶지 않은 예외의 의존관계를 참조하지 않아도 되는 장점이 있다.

단점: 언체크 예외는 개발자가 실수로 예외를 누락할 수 있다. 반면에 체크 예외는 컴파일러를 통해 예외 누락을 잡아준다.

## 예외 활용

기본적으로 런타임 예외를 사용하자.

비즈니스 로직상에서 의도적으로 던지는 예외만 checked exception을 사용해주자.

### 실무에서 Checked Exception의 문제

1. 복구 불가능한 예외
대부분의 예외는 복구가 불가능하다. 일부 복구가 가능한 예외도 있지만 아주 적다.
SQLException 을 예를 들면 데이터베이스에 무언가 문제가 있어서 발생하는 예외이다. SQL 문법에 문제가 있을 수도 있고, 데이터베이스 자체에 뭔가 문제가 발생했을 수도 있다. 데이터베이스 서버가 중간에 다운 되었을 수도 있다. 이런 문제들은 대부분 복구가 불가능하다. 
    
    특히나 대부분의 서비스나 컨트롤러는 이런 문제를 해결할 수는 없다. 따라서 이런 문제들은 일관성 있게 공통으로 처리해야 한다. 오류 로그를 남기고 개발자가 해당 오류를 빠르게 인지하는 것이 필요하다. 서블릿 필터, 스프링 인터셉터, 스프링의 ControllerAdvice 를 사용하면 이런 부분을 깔끔하게 공통으로 해결할 수 있다.
    
2. 의존 관계에 대한 문제
체크 예외의 또 다른 심각한 문제는 예외에 대한 의존 관계 문제이다.
앞서 대부분의 예외는 복구 불가능한 예외라고 했다. 그런데 체크 예외이기 때문에 컨트롤러나 서비스 입장에서는 본인이 처리할 수 없어도 어쩔 수 없이 throws 를 통해 던지는 예외를 선언해야 한다

throws SQLException, ConnectException 처럼 예외를 던지는 부분을 코드에 선언하는 것이 왜
문제가 될까?
바로 서비스, 컨트롤러에서 java.sql.SQLException 을 의존하기 때문에 문제가 된다.
향후 리포지토리를 JDBC 기술이 아닌 다른 기술로 변경한다면, 그래서 SQLException 이 아니라 예를
들어서 JPAException 으로 예외가 변경된다면 어떻게 될까?
SQLException 에 의존하던 모든 서비스, 컨트롤러의 코드를 JPAException 에 의존하도록 고쳐야 한다
