# 서블릿(Servlet)
## 서블릿 컨테이너

- 서블릿 컨테이너: 톰캣처럼 서블릿을 지원하는 WAS
- 서블릿 컨테이너는 서블릿 객체를 생성, 초기화, 호출, 종료하는 생명주기 관리
- 서블릿 객체는 싱글톤으로 관리
  - 고객의 요청이 올 때마다 계속 객체를 생성하는 것을 비효율적이므로 최초 로딩 시점에 서블릿 객체를 미리 만들어두고 재활용
  - 모든 고객 요청은 동일한 서블릿 객체 인스턴스에 접근
  - <p style="color: red;">공유 변수 사용 주의</p>
  - 서블릿 컨테이너 종료 시 함께 종료
- JSP도 서블릿으로 변환되어서 사용
- 동시 요청을 위한 멀티 쓰레드 처리 지원

## 스레드
- 애플리케이션 코드를 하나하나 순차적으로 실행하는 것은 스레드
- Java main 메서드를 처음 실행하면 main이라는 이름의 스레드가 실행
- 스레드가 없다면 자바 애플리케이션 실행이 불가능
- 스레드는 한 번에 하나의 코드라인만 수행
- 동시 처리가 필요하면 쓰레드를 추가로 생성

### 요청 마다 스레드 생성
- 장점
  - 동시 요청을 처리할 수 있음
  - 리소스(CPU, 메모리)가 허용할 때 까지 처리가능
  - 하나의 스레드가 지연 되어도, 나머지 스레드는 정상 동작
- 단점
  - 스레드 생성 비용은 매수 비쌈
    - 고객의 요청이 올 때 마다 스레드를 생성하면, 응답 속도가 늦어짐
  - 스레드는 컨텍스트 스위칭 비용이 발생함
  - 스레드 생성에 제한이 없음
    - 고객 요청이 너무 많이 오면, CPU, 메모리 임계점을 넘어서 서버가 죽을 수 있음

### 요청 마다 생성 단점 보완
- 특징
  - 필요한 스레드를 스레드 풀에 보관하고 관리
  - 스레드 풀에 생성 가능한 스레드의 최대치를 관리한다. Tomcat은 최대 200개 기본 설정
- 사용
  - 스레드가 필요하면, 이미 생성되어 있는 스레드를 스레드 풀에서 꺼내 사용
  - 사용을 종료하면 스레드 풀에 해당 스레드를 반납
  - 최대 스레드가 모두 사용중에어서 스레드 풀에 없으면
     - 기다리는 요청을 거절하거나 특정 숫자만큼 대기하도록 설정
- 장점
  - 스레드가 미리 생성되어 있으므로, 스레드를 생성하고 종료하는 비용이 절약되고, 응답 시간이 빠름
  - 생성 가능한 스레드의 최대치가 있으므로 너무 많은 요청이 들어와도 기존 요청은 안전하게 처리


### Tip
- WAS의 주요 튜닝 포인트는 <span style="color: red">최대 스레드 수</span>
  - 너무 낮게 설정하면 -> 동시 요청이 많으면, 서버 리소스는 여유롭지만, 클라이언트는 금방 응답 지연
  - 너무 높게 설정하면 -> 동시 요청이 많으면, CPU, 메모리 리소스 임계점 초과로 서버 다운
  - 장애 발생 시
    - 클라우드면 일단 서버부터 늘리고, 이후에 튜닝
    - 클라우드가 아니면 열심히 튜닝
  - 적정 숫자 찾기
    - 상황에 따라 다름 -> 성능 테스트: 아파치 ab, 제이미터, nGrinder 등
    - 최대한 실제 서비스와 유사하게 성능 테스트 시도


> WAS 멀티 스레드 지원<br>
개발자가 멀티 스레드 관련 코드를 신경쓰지 않아도 됨<br>
멀티 스레드 환경이므로 싱글톤 객체(Servlet, Spring Bean)는 주의해서 사용

## 서블릿 컨테이너 동작 방식
![ServletOperation](./img/ServletOperation.png)

## HTTP 요청 데이터
- GET - 쿼리 파라미터
  - /url?username=hello&age=20
  - 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
  - 예) 검색, 필터, 페이징 등에서 많이 사용

- POST - HTML Form
  - content-type: application/x-www-form-urlencoded
  - 메시지 바디에 쿼리 파라미터 형식으로 전달 username=hello&age=20

- HTTP message body에 데이터를 직접 담아서 요청
  - HTTP API에서 주로 사용, JSON, XML, TEXT
  - 데이터 형식은 주로 JSON 사용

## 서블릿과 JSP의 한계
- 서블릿으로 개발할 때는 View화면을 위한 HTML을 만드는 작업이 자바 코드에 섞여서 지저분하고 복잡했음
- 이를 개선하기 위해 JSP를 사용했지만 Java 코드, 데이터를 조회하는 repository 등 다양한 코드가 JSP에 노출되고 너무 많은 역할을 함

## MVC 패턴의 등장
- 비즈니스 로직은 서블릿 처럼 다른 곳에서 처리하고 JSP라는 목적에 맞게 HTML로 화면을 그리는 일에 집중하도록 하자. 과거 개발자도 비슷한 고민이 있었고 이에 따라 MVC 패턴이 등장함