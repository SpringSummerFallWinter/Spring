package Controller.controller;

import java.util.Map;
import Controller.ModelView;
import MemberDto.MemberDto;
import Repository.MemberRepository;


public class MemberSaveController implements Controller {
	
	MemberRepository memberRepository = MemberRepository.getInstance();

	@Override
	public String process(Map<String, String> paramMap, Map<String,Object> model) {
		
		String username = paramMap.get("username");
		int age = Integer.parseInt(paramMap.get("age"));
		MemberDto dto = new MemberDto(0, username, age);
		
		MemberDto receiveDto = memberRepository.save(dto);
		model.put("member", receiveDto);
		return "save-result";
	}
}
