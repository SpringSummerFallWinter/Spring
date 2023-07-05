package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {

        //given
        Member member = new Member();
        member.setName("kim");
        Address address = new Address("seoul","sinrim-road","01505");
        member.setAddress(address);
        //when
        memberService.join(member);

        //then
        List<Member> members = memberService.findMembers();
        assertThat(members.get(0)).isEqualTo(member);


    }

    @Test()
    public void 중복_회원_예외() throws Exception {

        //given
        Member member = new Member();
        member.setName("kim");
        Address address = new Address("seoul","sinrim-road","01505");
        member.setAddress(address);
        memberService.join(member);
        //when
        Member member2 = new Member();
        member2.setName("kim");
        Address address2 = new Address("1","2","3");
        member2.setAddress(address2);


        //then
        Assertions.assertThatThrownBy(() -> memberService.join(member2)).isInstanceOf(IllegalStateException.class);

    }
}