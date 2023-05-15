# Validation

**ValidationControllerV1**

error 이라는 HashMap을 두고 각각의 에러를 map에 넣고, 그 값을 출력할 view 화면도 준비했다.

```jsx
@PostMapping("/add")
public String addItem(@ModelAttribute Item item, RedirectAttributes 
redirectAttributes, Model model) {
 
 //검증 오류 결과를 보관
 Map<String, String> errors = new HashMap<>();
 //검증 로직
 if (!StringUtils.hasText(item.getItemName())) {
 errors.put("itemName", "상품 이름은 필수입니다.");
 }
 if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >
1000000) {
 errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
 }
 if (item.getQuantity() == null || item.getQuantity() >= 9999) {
 errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
 }
 //특정 필드가 아닌 복합 룰 검증
 if (item.getPrice() != null && item.getQuantity() != null) {
 int resultPrice = item.getPrice() * item.getQuantity();
 if (resultPrice < 10000) {
 errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 
현재 값 = " + resultPrice);
 }
 }
 //검증에 실패하면 다시 입력 폼으로
 if (!errors.isEmpty()) {
 model.addAttribute("errors", errors);
 return "validation/v1/addForm";
 }
 //성공 로직
 Item savedItem = itemRepository.save(item);
 redirectAttributes.addAttribute("itemId", savedItem.getId());
 redirectAttributes.addAttribute("status", true);
 return "redirect:/validation/v1/items/{itemId}";
}
```

**ValidationControllerV2-addItemV1**

Map errors 대신 BindingResult 를 이용한다.

FieldError(”Model 객체”,”필드이름”,”메세지”)

BindingResult가 있으면 @ModelAttribute에 데이터 바인딩 시 오류가 발생해도 컨트롤러가 호출된다.

@ModelAttribute에 바인딩 시 타입 오류가 발생하면?
BindingResult 가 없으면 400 오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로
이동한다.
BindingResult 가 있으면 오류 정보( FieldError )를 BindingResult 에 담아서 컨트롤러를
정상 호출한다

BindingResult 는 검증할 대상 바로 다음에 와야한다. 순서가 중요하다. 예를 들어서 @ModelAttribute
Item item , 바로 다음에 BindingResult 가 와야 한다.
BindingResult 는 Model에 자동으로 포함된다

```jsx
@PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes
            redirectAttributes) {

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item","itemName","상품이름은 필수 입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >
                1000000) {
            bindingResult.addError(new FieldError("item","price","가격은 1,000~10,000원 사이입니다"));

        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item","quantity","수량은 최대 9,999 까지 허용합니다."));
        }
        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "validation/v2/addForm";
        }
        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
```

그런데 이렇게 하면 고객이 입력한 정보가 모두 사라진다. 이문제를 해결하자

**ValidationControllerV2-addItemV2**

```jsx
@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes
            redirectAttributes) {

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item","itemName", item.getItemName(),false,null,null,"상품이름은 필수 입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >
                1000000) {
            bindingResult.addError(new FieldError("item","price",item.getPrice(),false,null,null,"가격은 1,000~10,000원 사이입니다"));

        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item","quantity",item.getQuantity(),false,null,null,"수량은 최대 9,999 까지 허용합니다."));
        }
        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", null,null,"가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "validation/v2/addForm";
        }
        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
```

<FieldError 생성자>
FieldError 는 두 가지 생성자를 제공한다.

```jsx
public FieldError(String objectName, String field, String defaultMessage);
public FieldError(String objectName, String field, @Nullable Object
rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable
Object[] arguments, @Nullable String defaultMessage)
```

objectName : 오류가 발생한 객체 이름
field : 오류 필드
rejectedValue : 사용자가 입력한 값(거절된 값)
bindingFailure : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
codes : 메시지 코드
arguments : 메시지에서 사용하는 인자
defaultMessage : 기본 오류 메시지

FieldError가 binding에 실패한값 (예 : Price에 ‘qqq’) 도 저장해줌.

이제 오류메세지를 관리해보자.

**ValidationControllerV2-addItemV3**

FieldError , ObjectError 의 생성자는 codes , arguments 를 제공한다. 이것은 오류 발생시 오류
코드로 메시지를 찾기 위해 사용된다.

```java
@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes
            redirectAttributes) {
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item","itemName", item.getItemName(),false,new String[]{"required.item.itemName"},null,null));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >
                1000000) {
            bindingResult.addError(new FieldError("item","price",item.getPrice(),false,new String[] {"range.item.price"},new Object[] {1000,1000000} ,null));

        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item","quantity",item.getQuantity(),false,new String[]{"max.item.quantity"},new Object[]{9999},null));
        }
        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"},new Object[]{10000,resultPrice},null));
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "validation/v2/addForm";
        }
        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
```

```java
Application.properties
spring.messages.basename=messages,errors

errors.properties
required.item.itemName=상품 이름은 필수입니다.
range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
max.item.quantity=수량은 최대 {0} 까지 허용합니다.
totalPriceMin=가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}
```

이렇게 설정을 해준다.

이렇게 하면 설정이 너무 복잡하다. 좀더 줄여보자

FieldError 와 ObjectError 가 다루기 너무 번거롭다.

**ValidationControllerV2-addItemV4**

컨트롤러에서 BindingResult 는 검증해야 할 객체인 target 바로 다음에 온다. 따라서
BindingResult 는 이미 본인이 검증해야 할 객체인 target 을 알고 있다. 즉 매번 객체명 item을 넣어줄 필요가 없다.

‘rejectValue()’ , ’reject()’ 를 사용하면 좀더 깔끔하게 짤 수 있다.

```java
@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes
            redirectAttributes) {
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.rejectValue("itemName","required");
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >
                1000000) {
            bindingResult.rejectValue("price","range", new Object[]{1000,1000000}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
           bindingResult.rejectValue("quantity","max", new Object[]{9999}, null);
        }
        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin",new Object[]{10000, resultPrice}, null);
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "validation/v2/addForm";
        }
        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
```

errors.properties 에 적혀있는 code 이름 중 앞 글자만 따왔는데 작동한다. (required, range, max)

mesageResolver 를 위한 오류코드이다.

BindingResult 는 target 객체에 대한 정보를 이미 알고있다. 따라서 target(item)을 써줄 필요가 없다.

### <어떤 식으로 오류코드를 작성할 것인가>

오류 코드를 만들 때 다음과 같이 자세히 만들 수도 있고,
required.item.itemName : 상품 이름은 필수 입니다.
range.item.price : 상품의 가격 범위 오류 입니다.
또는 다음과 같이 단순하게 만들 수도 있다.
required : 필수 값 입니다.
range : 범위 오류 입니다.

단순하게 만들면 범용성이 좋아서 여러곳에서 사용할 수 있지만, 메시지를 세밀하게 작성하기 어렵다. 반대로 너무 자세하게 만들면 범용성이 떨어진다. 가장 좋은 방법은 범용성으로 사용하다가, 세밀하게 작성해야 하는 경우에는 세밀한 내용이 적용되도록 메시지에 단계를 두는 방법이다.

```java
#Level1
required.item.itemName: 상품 이름은 필수 입니다.
#Level2
required: 필수 값 입니다.
```

이렇게 계층적으로 설계를 해두고, code 파라미터 입력란에 “required” 라고해두면. 해당 객체와 필드명이 있는 메세지만 level1 으로 출력되고, 없다면 level2 단계로 출력된다. 이렇게 하면 message 프로퍼티에 메세지 추가만 함으로써 편리하게 오류 메세지를 관리 할 수 있다.

스프링은 MessageCodesResolver 라는 것으로 이러한 기능을 지원한다.
