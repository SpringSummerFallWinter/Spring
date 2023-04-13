package com.example.frontcontroller.v4.controller;

import com.example.domain.member.Member;
import com.example.domain.member.MemberRepository;
import com.example.frontcontroller.ModelView;
import com.example.frontcontroller.v3.ControllerV3;
import com.example.frontcontroller.v4.ControllerV4;

import java.util.Map;

public class MemberSaveControllerV4 implements ControllerV4 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        model.put("member", member);

        return "save-result";
    }
}
