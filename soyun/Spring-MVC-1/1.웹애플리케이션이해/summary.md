# 웹 애플리케이션 이해  
<br/>

## 모든 것이 HTTP
- HTML, TEXT, IMAGE, 음성, 영상
- JSON, XML
- 거의 모든 형태의 데이터를 전송할 수 있다!

<br/>

## 웹 서버
- 정적 리소스(HTML, CSS, JS, 이미지, 영상) 제공
- ex) NGINX, APACHE

<br/>

## 웹 애플리케이션 서버
- 정적 리소스 제공 + 애플리케이션 로직 수행
  - WAS 만으로도 시스템 구성은 가능하지만...
    - 서버 과부하 우려
    - 서버 장애 시 오류 화면도 노출 불가
  - 고로 정적 리소스는 웹 서버가 처리, 애플리케이션 로직은 WAS가 처리
    - 관심사의 분리!
    - 효율적인 리소스 관리 가능
    - 웹 서버는 잘 죽지 않으므로 WAS 장애시 웹 서버를 통해 오류 화면 노출 가능
- 동적 HTML, HTTP API(JSON)
  - 서블릿, JSP, 스프링 MVC
- ex) Tomcat, Jetty

<br/>

## 서블릿
- 프로그래머가 비즈니스 로직에만 집중할 수 있도록 도와줌
  - 이 외의 부분들을 자동화(소켓 연결, HTTP 메세지 파싱 등등...)

### 특징
- @WebServlet에 등록한 URL이 호출되면 서블릿 코드가 실행됨
- HTTP 요청/응답 정보를 편리하게 사용할 수 있는 객체 제공
  - HttpServletRequest, HttpServletResponse
  - 개발자는 HTTP 스팩을 자세히 알지 않아도 된다!

### 서블릿 컨테이너
- 톰캣과 같이 서블릿을 지원하는 WAS
- 서블릿 객체를 생성, 초기화, 호출, 종료하는 생명주기 고나리
- 서블릿 객체는 싱글톤
  - 요청이 들어올 때마다 객체를 생성하는 것은 비효율적!
- 멀티 쓰레드 지원 -> 개발자는 동시성 처리를 신경쓰지 않아도 된다.

<br/>

## 동시 요청 - 멀티 쓰레드

### 쓰레드
- 애플리케이션 코드를 하나하나 순차적으로 실행하는 애
- 동시 처리가 필요하다면 쓰레드를 추가적으로 생성

### 다중 요청, 쓰레드 하나 사용
- 요청1이 쓰레드를 사용하고 있지만 응답이 지연되어 무한정 대기하는 상태...
- 요청2가 들어왔지만 쓰레드를 이미 사용하고 있어 마찬가지로 대기...
- 요청1과 요청2를 둘 다 처리하지 못하는 상황 발생 가능

### 다중 요청, 요청마다 쓰레드 생성
- 동시 요청 처리 가능
- 하나의 쓰레드가 지연되어도, 나머지 쓰레드는 정상 동작
- 그러나...
  - 쓰레드는 생성 비용이 크기 때문에 응답 속도가 느려지게 됨
  - 컨텍스트 스위칭 비용이 발생
  - 과도한 요청이 들어온다면 CPU, 메모리 임계점을 넘어 서버가 터짐

### 쓰레드 풀
- 필요한 쓰레드를 풀에 보관하고 관리
  - 이미 쓰레드를 생성한 상태이므로 생성/종료 비용이 절약됨 -> 응답 속도 빠름
- 쓰레드가 필요하면 풀에서 꺼내서 사용 후 반납
- 최대 쓰레드가 모두 사용 중이라면, 요청을 거절하거나 대기
  - 과도한 요청이 들어와도 서버가 터질 일 거의 없음
- 쓰레드 수 튜닝
  - 너무 낮게 설정
    - 클라이언트 응답 지연, 남아도는 리소스 활용 불가
  - 너무 높게 설정
    - 동시 요청이 많다면, 임계점을 넘어 서버가 터질 수 있음(과유불급)
  - 그래서 최대 쓰레드 수는 어떻게 정하는데?
    - 애플리케이션 로직에 따라 다르다.
    - Apache ab, nGrinder(요즘 많이 씀)와 같은 성능 테스트 tool 활용

### WAS의 멀티 쓰레드 지원
- 멀티 쓰레드 관련 부분은 WAS가 처리
- 개발자는 1도 신경 안써도 됨
- 그러나 싱글톤 객체는 주의해서 사용할 것!!! (공유 변수 주의)

<br/>

## HTML, HTTP API, CSR, SSR

### 정적 리소스
- 정적 HTML 파일, CSS, JSS, 이미지, 영상 등의 파일을 제공하는 것 (웹 서버)

### HTML 페이지
- 동적으로 필요한 HTML 파일을 생성 후 전달 (WAS)
  - JSP, Thymeleaf 등의 View 템플릿 사용
- 웹 브라우저는 HTML을 해석

### HTTP API
- HTML 파일이 아닌 데이터를 전달 (주로 JSON 형식)
- UI 화면은 클라이언트가 별도로 처리
- 앱(iOS, Android 등), 웹 클라이언트(JS, React 등) to 서버
- 서버 to 서버 (주문 서버 -> 결제 서버)

### SSR
- HTML 최종 결과를 서버에서 만들어서 웹 브라우저에 전달
- 주로 정적이고, 복잡하지 않은 화면에 사용
- JSP, Thymeleaf
- 백엔드 개발자의 소양

### CSR
- HTML 결과를 자바스크립트를 사용해 웹 브라우저에서 동적으로 생성하여 적용
- 웹 화면을 필요한 부분만 변경 가능
- React, Vue.js
- 선택과 집중, 프론트한테 맡겨

<br/>

## 자바 웹 기술 역사
1. 서블릿 (1997)
   - HTML 생성 어려움 (PrintWriter 사용...끔찍)
2. JSP (1999)
   - HTML 생성은 편리
   - 그러나 JSP가 비즈니스 로직까지 담당 -> 너무 복잡해
3. 서블릿, JSP 조합의 MVC 패턴
   - Model, View, Controller로 분리하여 개발
   - 비슷한 패턴의 연속... -> 이를 프레임워크로 만들자!
4. 스트럿츠, 구닥다리 스프링 MVC
   - 스트럿츠 메인 + 스프링 MVC 조합
5. 애노테이션 기반 스프링 MVC
   - 너무 편함, 스프링이 MVC 프레임워크 대통합
6. 스프링 부트
   - 서버를 내장하고 있음
     - 직접 WAS 서버를 설치하고 War 파일을 배포할 필요 없음
     - 빌드 결과(Jar)에 WAS 서버가 포함되어 있어서 실행만 시키면 됨 (빌드 배포 단순화)

### 스프링 웹 기술의 분화
- Web Servlet - Spring MVC
  - 서블릿 기반
  - 기본적인 스프링
- Web Reactive - Spring WebFlux
  - 최신 기술, 서블릿 사용 X
  - 비동기 Non-Blocking 처리
  - 최소 쓰레드로 최대 성능 -> 컨텍스트 스위칭 비용 효율화
  - 함수형 스타일 개발, 코드 깔끔해지고 동시처리 코드 효율화
  - Node.js와 비슷?
  - 그러나...
    - 아직은 MVC로도 충분하기 때문에 많이 사용하지는 않음
    - 기술적 난이도 매우 높음

<br/>
<br/>
<br/>
<br/>
<br/>