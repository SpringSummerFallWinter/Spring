# Bean Validation

**스프링 부트는 자동으로 글로벌 Validator로 등록한다.**

LocalValidatorFactoryBean 을 글로벌 Validator로 등록한다. 이 Validator는 @NotNull 같은
애노테이션을 보고 검증을 수행한다. 이렇게 글로벌 Validator가 적용되어 있기 때문에, @Valid ,
@Validated 만 적용하면 된다.
검증 오류가 발생하면, FieldError , ObjectError 를 생성해서 BindingResult 에 담아준다.

검증시 @Validated @Valid 둘다 사용가능하다.

> javax.validation.@Valid 를 사용하려면 build.gradle 의존관계 추가가 필요하다. (이전에
추가했다.)
implementation 'org.springframework.boot:spring-boot-starter-validation'
@Validated 는 스프링 전용 검증 애노테이션이고, @Valid 는 자바 표준 검증 애노테이션이다. 둘중
아무거나 사용해도 동일하게 작동하지만, @Validated 는 내부에 groups 라는 기능을 포함하고 있다. 이
부분은 조금 뒤에 다시 설명하겠다.
> 

**검증 순서**

1. @ModelAttribute 각각의 필드에 타입 변환 시도
    1. 성공하면 다음으로
    2. 실패하면 typeMismatch 로 FieldError 추가
2. Validator 적용

@ModelAttribute → 각각의 필드 타입 변환시도 → 변환에 성공한 필드만 BeanValidation 적용

Ex)

itemName 에 문자 "A" 입력 타입 변환 성공 itemName 필드에 BeanValidation 적용
price 에 문자 "A" 입력 "A"를 숫자 타입 변환 시도 실패 typeMismatch FieldError 추가
price 필드는 BeanValidation 적용 X

## Bean Validation 에러코드

Bean Validation을 적용하고 bindingResult 에 등록된 검증 오류 코드를 보자.
오류 코드가 애노테이션 이름으로 등록된다. 마치 typeMismatch 와 유사하다.
NotBlank 라는 오류 코드를 기반으로 MessageCodesResolver 를 통해 다양한 메시지 코드가 순서대로 생성된다.
@NotBlank
NotBlank.item.itemName
NotBlank.itemName
NotBlank.java.lang.String
NotBlank
@Range
Range.item.price
Range.price
Range.java.lang.Integer
Range

```jsx
#Bean Validation 추가
NotBlank={0} 공백X 
Range={0}, {2} ~ {1} 허용
Max={0}, 최대 {1}
```

[errors.propertie](http://errors.properties)s 에 위와같이 작성해준다.

```jsx
좀 더 자세한 내용을 적고 싶다면
NotBlank.item.itemName = 상품 이름을 적어주세요.
이렇게 적어준다.
```

## Bean Validation 오브젝트 오류

그렇다면 필드오류가 아니라 오브젝트 오류는 어디에 작성할까?

@ScriptAssert를 사용하면 된다.

@ScriptAssert(lang = "javascript", script ="_this.price * _this.quantity >= 10000", message = "총합이 10000원 넘게 입력해주세요.")

이런식으로 사용해야 하는데, 좀 조잡하기도 하고 기능이 약하다. 그래서 억지로 ScriptAssert를 사용하지말고 자바 코드로 직접 작성하자.

## Bean Validation Groups

등록과 수정에서 다른 요구사항을 제시하면, 충돌이 일어난다.

Groups 로 해결하자.

방법 2가지
- BeanValidation의 groups 기능을 사용한다.
- Item을 직접 사용하지 않고, ItemSaveForm, ItemUpdateForm 같은 폼 전송을 위한 별도의 모델
객체를 만들어서 사용한다

실제로 실무에서는 Groups 가 잘 사용되지 않는다.

## RequestBody 요청시

API의 경우 3가지 경우를 나누어 생각해야 한다.
 성공 요청: 성공
 실패 요청: JSON을 객체로 생성하는 것 자체가 실패함
 검증 오류 요청: JSON을 객체로 생성하는 것은 성공했고, 검증에서 실패함

JSON을 객체로 만드는 것 자체를 실패하면, 컨트롤러 호출이 안되고 400 에러가 뜸.

뒤에 exception 핸들링에서 다루자.

**<@ModelAttribute vs @RequestBody>**
HTTP 요청 파리미터를 처리하는 @ModelAttribute 는 각각의 필드 단위로 세밀하게 적용된다. 그래서
특정 필드에 타입이 맞지 않는 오류가 발생해도 나머지 필드는 정상 처리할 수 있었다.
HttpMessageConverter 는 @ModelAttribute 와 다르게 각각의 필드 단위로 적용되는 것이 아니라,
전체 객체 단위로 적용된다.
따라서 메시지 컨버터의 작동이 성공해서 ItemSaveForm 객체를 만들어야 @Valid , @Validated 가
적용된다.
@ModelAttribute 는 필드 단위로 정교하게 바인딩이 적용된다. 특정 필드가 바인딩 되지 않아도 나머지
필드는 정상 바인딩 되고, Validator를 사용한 검증도 적용할 수 있다.
@RequestBody 는 HttpMessageConverter 단계에서 JSON 데이터를 객체로 변경하지 못하면 이후
단계 자체가 진행되지 않고 예외가 발생한다. 컨트롤러도 호출되지 않고, Validator도 적용할 수 없다.
