# Spring MVC 1편

1. MVC 프레임워크 만들기

  1. view만들기[https://github.com/SpringSummerFallWinter/Spring/tree/main/springmvc1/youngho/build_MVC_Framework/addView]
  2. model만들기
  각 컨트롤러에서 서블릿 종속성을 제거하였다. HttpServletRequest, HttpServletResponse 를 사용하지 않고 컨트롤러를 만들어 보았다. 
  request.setAttribute 메소드 대신 Map 을 사용하여 String 과 Object를 매치해두었다. 
