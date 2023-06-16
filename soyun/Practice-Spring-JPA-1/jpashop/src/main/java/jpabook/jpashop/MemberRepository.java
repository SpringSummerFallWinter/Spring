package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);

        // command와 query를 분리 -> 보통 return값을 만들지 않음, 다음에 조회할 수 있도록 Id 정도만 리턴
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
