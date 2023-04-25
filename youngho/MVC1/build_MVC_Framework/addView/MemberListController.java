package Controller.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.Myview;
import Domain.Member;
import MemberDto.MemberDto;
import Repository.MemberRepository;


public class MemberListController implements Controller {
	
	MemberRepository memberRepository = MemberRepository.getInstance();
	
	public Myview service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Member> list = memberRepository.findAll();
		request.setAttribute("members", list);
		String viewPath = "/WEB-INF/views/members.jsp";
		return new Myview(viewPath);
	}

}
