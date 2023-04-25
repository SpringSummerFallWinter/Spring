package Controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Myview {

	private String viewPath;
	
	public Myview(String viewPath) {
		this.viewPath = viewPath;
	}

	public void render(Map<String, Object> model, HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		modelToRequestAttribute(request,model);
		RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
		dispatcher.forward(request, response);
	}

	private void modelToRequestAttribute(HttpServletRequest request, Map<String, Object> model) {
		model.forEach((key,value)->request.setAttribute(key, value));
	}
}
