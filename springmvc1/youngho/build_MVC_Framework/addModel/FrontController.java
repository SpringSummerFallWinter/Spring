package Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.controller.Controller;
import Controller.controller.MemberFormController;
import Controller.controller.MemberListController;
import Controller.controller.MemberSaveController;

@WebServlet("/v1/*")
public class FrontController extends HttpServlet {
       
	private Map<String, Controller> map = new HashMap<>();
	
	public FrontController() {
		map.put("/springMVC/v1/members", new MemberListController());
		map.put("/springMVC/v1/members/new-form", new MemberFormController());
		map.put("/springMVC/v1/members/save", new MemberSaveController());
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		String requestURI = request.getRequestURI();
		Controller controller = map.get(requestURI);
		if(controller == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		Map<String, String> paramMap = createParamMap(request);
		
		ModelView modelView = controller.process(paramMap);
		String viewName = modelView.getViewName();
		Myview myview = viewResolver(viewName);
		myview.render(modelView.getModel(),request, response);
	}

	private Map<String, String> createParamMap(HttpServletRequest request) {
		Map<String, String> paramMap = new HashMap<>();
		request.getParameterMap().forEach((paramKey,paramValue) -> paramMap.put(paramKey, paramValue[0]));
		return paramMap;
	}
    
	private Myview viewResolver(String viewName) {
		return new Myview("/WEB-INF/views/" + viewName + ".jsp");
	}
}
