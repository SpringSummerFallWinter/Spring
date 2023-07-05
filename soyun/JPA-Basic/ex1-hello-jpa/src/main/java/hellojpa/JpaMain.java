package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");// persistence.xml에 정의
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
/*          //1차 캐시에서 조회

            // 비영속
            Member member = new Member();
            member.setId(101L);
            member.setName("HelloPSY");

            // 영속 - > 영속 컨텍스트에 올라감1
            System.out.println("===BEFORE===");
            em.persist(member); // 바로 insert 되는 것이 아님!!
            System.out.println("===AFTER===");

            // 1차 캐시에 저장이 되었으므로 select 쿼리 날라가지 않음
            Member findMember = em.find(Member.class, 101L);
            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getName() = " + findMember.getName());*/

/*          //영속 엔티티의 동일성 보장

            // 영속 컨텍스트에 업로드
            Member findMember1 = em.find(Member.class, 101L);
            // 캐싱된 값을 가져오므로 쿼리 날라가지 않음
            Member findMember2 = em.find(Member.class, 101L);

            System.out.println("result = " + (findMember1 == findMember2)); // true*/

/*          //트랜잭션을 지원하는 쓰기 지연

            // 영속
            Member member1 = new Member(150L, "A");
            Member member2 = new Member(160L, "B");

            em.persist(member1);
            em.persist(member2);

            System.out.println("=========================");*/

/*          //변경 감지

            // 영속 엔티티 조회
            Member member = em.find(Member.class, 150L);
            // 영속 엔티티 데이터 수정
            member.setName("ZZZZZ");    // 값만 바꿔도 update query 실행됨

            System.out.println("=========================");*/

            // 준영속 상태로

            // 영속 엔티티 조회
            Member member = em.find(Member.class, 150L);
            // 영속 엔티티 데이터 수정
            member.setName("ZZZZZ");
            em.detach(member);  // 준영속 상태로 만듦, 더 이상 영속성 컨텍스트의 기능을 사용할 수 없다.
//            em.clear();

            Member member2 = em.find(Member.class, 150L);   // 영속 컨텍스트에 존재하지 않으므로 select query 날라감

            System.out.println("=========================");

/*          // flush()

            Member member = new Member(200L, "member200");
            em.persist(member);

            em.flush(); // 바로 DB에 반영, flush() 후에도 1차 캐시는 유지됨

            System.out.println("=========================");*/

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

