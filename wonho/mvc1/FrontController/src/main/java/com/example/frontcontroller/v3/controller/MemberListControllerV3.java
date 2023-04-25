package com.example.frontcontroller.v3.controller;

import com.example.domain.member.Member;
import com.example.domain.member.MemberRepository;
import com.example.frontcontroller.ModelView;
import com.example.frontcontroller.MyView;
import com.example.frontcontroller.v2.ControllerV2;
import com.example.frontcontroller.v3.ControllerV3;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MemberListControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        List<Member> members = memberRepository.findAll();

        ModelView mv = new ModelView("members");
        mv.getModel().put("members", members);
        return mv;
    }
}
