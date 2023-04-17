## 스프링 기본 복습

```java
public class BasicItemController {
    *private final ItemRepository itemRepository;
    @Autowired
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
}
```

스프링은 여기서 생성자가 1개일 경우 @Autowired 어노테이션을 생략해 줄 수 있다.

```java
public class BasicItemController {
    *private final ItemRepository itemRepository;
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
}
```

여기서 @RequiredArgsConstructor 어노테이션을 쓰면 자동으로 생성자를 만들어 준다.

```java
@RequiredArgsConstructor
public class BasicItemController {
    *private final ItemRepository itemRepository;
}
```

결론적으론 이렇게 남게된다.

## 타임리프

- th: xxx 가 붙은 부분은 서버사이드에서 렌더링 되고, 기존 것을 대체한다. th: xxx 이 없으면 기존 html의 xxx 속성이 그대로 사용된다.
- th:href="@{/css/bootstrap.min.css}”
- @{...} : 타임리프는 URL 링크를 사용하는 경우 @{...} 를 사용한다. 이것을 URL 링크 표현식이라 한다.
URL 링크 표현식을 사용하면 서블릿 컨텍스트를 자동으로 포함한다
- 쿼리 파라미터도 한번에 지원한다.
- 예) th:href="@{/basic/items/{itemId}(itemId=${[item.id](http://item.id/)}, query='test')}"
    
    

## 상품 등록 폼

th: action 하고 비워두면 현재 url 그대로 전송된다.

```java
@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                          @RequestParam int price,
                          @RequestParam int quantity,
                          Model model) {

        Item item = new Item(itemName,price,quantity);
        item = itemRepository.save(item);
        model.addAttribute("item",item);
        return "basic/item";
    }
```

위 코드는 아래처럼 변경 가능.

```java
@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        item = itemRepository.save(item);
        model.addAttribute("item",item);
        return "basic/item";
    }
```

그렇다면 @ModelAttribute(”item”) 여기 있는 item은 왜 필요할까?

model.addAttribute("item",item); 이걸 해주는 역할이다.

```java
@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {
        itemRepository.save(item);
        return "basic/item";
    }
```

결국엔 이렇게 간단한 형태로 변경이 가능하다.

Model model argument 도 생략되었다. 

결국 @ModelAttribute 는 2가지 역할을 한다.

1. Item 객체를 만들고, 내부 값을 채워준다
2. Model 에 Item 객체를 담아준다.

**Redirect 하는방법**

```java
@PostMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, @ModelAttribute("item") Item item) {
        itemRepository.update(itemId,item);
        return "redirect:/basic/items/{itemId}";
    }
```

{itemId} 로 해주면 PathVariable 에 있는 값 그대로 쓰게 해준다.

# ※※지금까지 진행한 상품 등록 컨트롤러는 심각한 문제가 있다. 새로고침을 누르면 등록이 계속 진행되는 것을 볼 수 있다.

## PRG Post/Redirect/Get

지금 컨트롤러 흐름을 보자.

마지막으로 POST 요청을 보내고 서버 내부에서 뷰 템플릿을 호출하는 형태이다.

<img width="470" alt="0417" src="https://user-images.githubusercontent.com/108070719/232489690-c23f264b-897e-4838-9d36-c42c18380578.PNG">

결국 새로고침은 마지막에 했던 행위를 반복 하는 것인데, 이경우 Post 를 반복하게 된다.

새로고침 문제를 해결하려면, 상품 저장 후에 뷰 템플릿으로 이동하는게 아니라 상품 상세 화면으로 리다이렉트를 호출해주면 된다.

return "redirect:/basic/items/" + item.getId();

## RedirectAttribute

```java
@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status",true);
        return "redirect:/basic/items/{itemId}";
    }
```

RedirectAttribute 를 쓰면 {} 안에 값을 집어 넣을 수 있다. 또 한글을 집어 넣을 경우에 이렇게 해주어야 인코딩 문제를 해결 할 수 있다.

status 는 queryParameter 으로 넘어간다.

```java
<h2 th:if="${param.status}" th:text="'저장 완료'"></h2>
```

타임리프 if 문을 사용해서 넘어간 status 파라미터를 처리해준다.

param. 을 해주면 파라미터를 받을 수 있다.
