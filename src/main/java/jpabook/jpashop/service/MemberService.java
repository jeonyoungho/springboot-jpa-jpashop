package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 조회하는데 있어서 더티체킹을 안하기에 성능을 더 높일 수 있음
//@RequiredArgsConstructor // 아래 주입을 자동으로 해줌, 중간에 필드가 혹시 필요한건 final 안붙여서 쓰면 됨
public class MemberService {

    /**
     * 필드 주입: 스프링 컨테이너와 강하게 결합되어 절대 접근하여 바꿀 수 없어서 테스트하기 힘듬
     * 수정자 주입: 테스트 할 때 의존하는 객체를 유연하게 바꿀 수 있는 장점이 있지만 데이터가 언제든지 바뀔 수 있는 치명적인 단점이 있음
     * 생성자 주입: 테스트 할 때 생성자에 mock객체를 주입하는 등 유연하게 테스트할 할 수 있고, 생성할때 완성되기때문에 데이터 불변성을 보장할 수 있다.
     */

    private final MemberRepository memberRepository; // 변경할 일이 없기에 final권장 그래야 컴파일 시점에 체크할 수 있음
    
    public MemberService(MemberRepository memberRepository) { // parameter가 하나일땐 @Autowired 어노테이션을 스프링이 자동으로 붙여줌
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     */
    @Transactional // JPA의 모든 데이터 변경이나 로직들은 가급적이면 트랜잭션 안에서 수행되어야함, 그래야 Lazy로딩도 적용됨
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    // 운영에서 WAS가 여러 개 뜰텐데 동시에 alice란 이름으로 두 개가 insert되면? 이를 해결하기 위해 DB에 unique 제약조건을 걸어주는게 좋다.
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
     * Command와 Query를 분리하는게 좋다. update란건 엔티티를 바꾸는 변경성 메서드인데 findOne()메서드로 조회해서 반환하는 꼴이 되버린다.
     * 즉 커맨드와 쿼리가 같이 있는 꼴이 되어 분리하는게 좋다.
     */
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
