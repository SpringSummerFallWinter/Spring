package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {

    // 서블릿에 종속적이지 않다!! (No request, response 객체)
    ModelView process(Map<String, String> paramMap);
}
