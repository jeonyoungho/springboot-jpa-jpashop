package jpabook.jpashop;

//@RunWith(SpringRunner.class) // junit 에게 spring 관련된걸로 test 할거야라고 알려줘야함
//@SpringBootTest
//public class MemberRepositoryTest {
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Test
//    @Transactional // @Transactional 어노테이션이 테스트에 있으면 테스트가 끝난 다음에 롤백해버리기에 실제 DB 에는 데이터가 없다.
//    @Rollback(false) // 테스트가 끝나도 롤백하지 않도록 지정
//    public void testMember() throws Exception {
//        // given
//        Member member = new Member();
//        member.setName("memberA");
//
//        // when
//        Long saveId = memberRepository.save(member);
//        Member findMember = memberRepository.find(saveId);
//
//        // then
//        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
//        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
//        Assertions.assertThat(findMember).isEqualTo(member); // 영속성 컨텍스트에 있는 member 를 가져오기이 같은 객체를 가져오게 된다.
//        System.out.println("findMember == member: " + (findMember == member));
//    }
//
//}