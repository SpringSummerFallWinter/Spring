package Controller.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.corba.se.spi.activation.Repository;

import Controller.Myview;
import MemberDto.MemberDto;
import Repository.MemberRepository;


public class MemberSaveController implements Controller {
	
	MemberRepository memberRepository = MemberRepository.getInstance();

	@Override
	public Myview service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		int age = Integer.parseInt(request.getParameter("age"));
		MemberDto dto = new MemberDto(0, username, age);
		
		MemberDto receiveDto = memberRepository.save(dto);
		request.setAttribute("member", receiveDto);
		String viewPath = "/WEB-INF/views/save-result.jsp";
		return new Myview(viewPath);
	}
}
