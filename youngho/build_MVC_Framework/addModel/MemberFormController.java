package Controller.controller;

import java.util.Map;
import Controller.ModelView;

public class MemberFormController implements Controller {

	@Override
	public ModelView process(Map<String, String> paramMap) {
		return new ModelView("new-form");
	}
}
