package jpabook.jpashop.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createFrom(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        // Member객체가 아닌 별도의 MemberForm을 만들어 사용하는 이유는?
        // MemberForm과 Member객체는 있어야할 멤버가 다르기도 하고 컨트롤에서 넘어올때랑
        // 실제 도메인이 원하는 validation이랑 다를 수 있기 때문에 필요한 데이터들만 별도의 Form클래스로 만들어 사용하는게 낫다.

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        
        // 요구사항이 진짜 단순할때 엔티티로 폼데이터를 받아도된다.
        // 하지만 실무에서의 요구사항은 절대 단순하지 않다. 뭐라도 차이가 난다.
        // 문제는 엔티티가 화면을 처리하기 위한 기능이 증가되고 결과적으로 화면종속적인 기능이 계속 생기게 된다.
        // 이렇게 되면 정말 유지보수하기 어려워진다.
        // 엔티티를 다른 의존성 없이 오직 핵심 비즈니스로직에만 의존성을 갖게 설계해야 한다. 그래야 애플리케이션이 커져도 엔티티를 여러군데서 사용하더라도 유지보수성이 좋아진다.
        // 그래서 화면에 맞는 API나 이런건 form객체나 DTO를 사용해야 한다.

        // 예제는 단순하니깐 Member엔티티를 그대로 반환했다.
        // 하지만 실무적으로 복잡해지면 DTO로 변환해서 화면에 필요한 데이터만 뿌리것을 권장한다.
        // tempalte엔진에서 렌더링할땐 서버안에서 돌기때문에 엔티티그대로 전달해도 괜찮다.
        // API를 만들땐 이유를 불문하고 절대 엔티티를 외부에 반환하지마라. API란건 스펙이다.
        // 예를 들어, Member엔티티에 password 필드를 추가하면 패스워드가 그대로 노출되는 문제와 API스펙이 변하는 문제가 있다.
        // 그러면 굉장히 불안한 스펙이 되버린다.

        return "members/memberList";
    }
}
