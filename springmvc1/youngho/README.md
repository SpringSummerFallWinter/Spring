# Spring MVC 1편

## 1. MVC 프레임워크 만들기

  1. [view만들기](https://github.com/SpringSummerFallWinter/Spring/tree/main/springmvc1/youngho/build_MVC_Framework/addView)
  2. [model만들기](https://github.com/SpringSummerFallWinter/Spring/tree/main/springmvc1/youngho/build_MVC_Framework/addModel)   
   각 컨트롤러에서 서블릿 종속성을 제거하였다. HttpServletRequest, HttpServletResponse 를 사용하지 않고 컨트롤러를 만들어 보았다. 
  request.setAttribute 메소드 대신 Map 을 사용하여 String 과 Object를 매치해두었다. 
  3. [간단하고실용적인 컨트롤러](https://github.com/SpringSummerFallWinter/Spring/tree/main/springmvc1/youngho/build_MVC_Framework/simpleAndPractical)   
    각 컨트롤러에서는 return 값으로 jsp 파일 명만 반환 하면 된다. 그전에는 model map 을 반환했는데 어떻게 가능할까?   
    model map 을 인자로 받아오면 된다. 
## 2. 스프링 MVC - 기본 기능
  1. [스프링 기본 기능](https://github.com/SpringSummerFallWinter/Spring/blob/main/springmvc1/youngho/basic_feature/basic_feature_spring.md)
## 3. 스프링 웹 서비스 만들기  
  1.[스프링 웹 서비스 만들기](https://github.com/SpringSummerFallWinter/Spring/blob/main/springmvc1/youngho/build_webPage/buildWebPage.md)
