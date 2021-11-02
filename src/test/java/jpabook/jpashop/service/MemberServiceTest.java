package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");
        
        // when
        Long saveId = memberService.join(member); // @Transactional 이 테스트케이스에 있으면 기본적으로 롤백하기에 insert 쿼리가 나가질 않는다. 롤백을 안시킬려면 @Rollback(value = false) 을 사용하면 된다.

        // then
//        em.flush(); // 그래도 쿼리를 보고 싶다면 flush()를 통해 쓰기 지연 SQL 저장소에 있는 내용을 강제로 비워주면 된다.
        assertEquals(member, memberRepository.findOne(saveId));
    }
    
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        
        // when
        memberService.join(member1);
        memberService.join(member2); // 예외가 발생해야 한다!!!

        // then
        fail("예외가 발생해야 한다."); // Assert의 fail() 메소드가 있는데 여기까지 코드가 내려오면 안된다.

    }
}