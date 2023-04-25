package Controller.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.Myview;

public interface Controller {

	Myview service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
