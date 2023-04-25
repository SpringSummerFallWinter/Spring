package Controller.controller;

import java.util.Map;

import Controller.ModelView;

public interface Controller {

	ModelView process(Map<String,String> paramMap);
}
