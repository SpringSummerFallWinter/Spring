package Controller.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.ModelView;
import Controller.Myview;
import Domain.Member;
import MemberDto.MemberDto;
import Repository.MemberRepository;


public class MemberListController implements Controller {
	
	MemberRepository memberRepository = MemberRepository.getInstance();
	
	@Override
	public String process(Map<String, String> paramMap, Map<String,Object> model) {
		List<Member> list = memberRepository.findAll();
		model.put("members", list);
		return "members";
	}

}
