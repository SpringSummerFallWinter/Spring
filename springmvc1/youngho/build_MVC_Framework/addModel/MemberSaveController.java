package Controller.controller;

import java.util.Map;
import Controller.ModelView;
import MemberDto.MemberDto;
import Repository.MemberRepository;


public class MemberSaveController implements Controller {
	
	MemberRepository memberRepository = MemberRepository.getInstance();

	@Override
	public ModelView process(Map<String, String> paramMap) {
		
		String username = paramMap.get("username");
		int age = Integer.parseInt(paramMap.get("age"));
		MemberDto dto = new MemberDto(0, username, age);
		
		MemberDto receiveDto = memberRepository.save(dto);
		ModelView modelView = new ModelView("save-result");
		modelView.getModel().put("member", receiveDto);
		return modelView;
	}
}
