# 스프링 MVC - 웹 페이지 만들기

<br/>

## 개요
- 지금까지 배운 내용을 기반으로 하나의 웹 애플리케이션을 제작했다.
- thymeleaf 뷰 템플릿을 사용하여 동적인 HTML 페이지를 제작했다.

<br/>

## thymeleaf

<br/>

### 핵심
- `th:xxx` 형식의 속성은 서버사이드에서 렌더링, 기존 속성 대체
- HTML 구조를 유지하면서 개발할 수 있고, HTML 파일을 직접 열어도 깨지지 않는다.

<br/>

### 문법
- 리터럴 대체
  - `"|location.href='@{/basic/items/{itemId}(itemId=${itemId})}'|"`
- URL 링크 표현식
  - `@{/basic/items/{itemId}(itemId=${itemId})}`
- 다양한 `th:xxx` 속성 제공
  - `th:text`, `th:onclick`, `th:each` 등등...

<br/>

## PRG (Post/Redirect/Get)

<br/>

### 문제 발생
- 상품 등록 후 새로고침을 누르면, 상품 등록 로직이 중복되어 실행됨!
  - **새로고침 버튼을 누르면 마지막에 서버에 전송한 데이터를 다시 전송하기 때문!!**

<br/>

### 해결
- 기존: 상품 저장 후 뷰 템플릿으로 이동
- 수정: 상품 저장 후 상세화면으로 redirect
  - `return "redirect:/basic/items/" + item.getId();`
  - PRG 패턴이라고 부름

<br/>

## RedirectAttributes

<br/>

### 사용 이유
- `return "redirect:/basic/items/" + item.getId();`
  - 위와 같은 방법으로 URL에 변수를 더해서 사용하는 것은 위험하다.
  - **URL 인코딩이 안되기 때문!**

<br/>

### `RedirectAttributes`
- URL 인코딩, PathVariable, Query Parameter까지 처리해준다.
    ```java
        @PostMapping("/add")
        public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
    
            Item savedItem = itemRepository.save(item);
            // URL 인코딩 해줌
            redirectAttributes.addAttribute("itemId", savedItem.getId());   // PathVariable 형식
            redirectAttributes.addAttribute("status", true);    // Query Parameter 형식
    
            return "redirect:/basic/items/{itemId}";
        }
    ```
  - 결과 URL: `http://localhost:8080/basic/items/4?status=true`
- PathVariable 바인딩 처리
- 그 외의 것들은 쿼리 파라미터로 처리 (위의 `status`는 쿼리파라미터로 처리된다)

<br/>

<br/>
<br/>