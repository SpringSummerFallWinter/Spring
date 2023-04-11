package Controller.controller;

import java.util.Map;

import Controller.ModelView;

public interface Controller {

	String process(Map<String,String> paramMap, Map<String,Object> model);
}
