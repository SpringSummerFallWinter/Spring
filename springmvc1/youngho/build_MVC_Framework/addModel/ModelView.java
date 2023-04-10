package Controller;

import java.util.HashMap;
import java.util.Map;

public class ModelView {

	private Map<String, Object> model = new HashMap<>();
	private String viewName;
	public ModelView(String viewName) {
		this.viewName = viewName;
	}
	public Map<String, Object> getModel() {
		return model;
	}
	public void setModel(Map<String, Object> model) {
		this.model = model;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	
	
	
}
