package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 등록 V1: 요청 값으로 Member 엔티티를 직접 받는다.
     * 문제점
     * - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
     * - 엔티티에 API 검증을 위한 로직이 들어간다. (@NotEmpty 등등)
     * - 실무에서는 회원 엔티티를 위한 API가 다양하게 만들어지는데, 한 엔티티에 각각의 API를
     위한 모든 요청 요구사항을 담기는 어렵다.
     * - 엔티티가 변경되면 API 스펙이 변한다.
     * 결론
     * - API 요청 스펙에 맞추어 별도의 DTO를 파라미터로 받는다.
     */
    @GetMapping("/api/v1/members")
    public List<Member> memberV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    /**
     * 장점: 추가적인 클래스를 안 만들어도 된다.
     *
     * 단점: 어떤 API 쓰는데선 필요한데 다른 API는 필요없을수도 있음, 화면 validation을 위한 로직 , 엔티티에 validation로직이 있으면 안좋음,
     * 엔티티랑 API스펙이 1대1로 매핑되있어서 엔티티를 수정하면 API 스펙이 바껴버리는게 문제가 됨, 기존 API를 사용하던 클라이언트 입장에게 전혀 다른 데이터를 보내주게 됨
     * API스펙을 위한 별도의 DTO를 만들어야한다. 즉, API를 절대 컨트롤러단 파라미터로 받지말고 외부에 노출해서도 안된다.
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 장점:
     * 1. 누가 name을 username으로 바꿔도 컴파일 오류로 알 수 있다. 엔티티를 변경하더라도 API 스펙 또한 바뀌지 않는다.
     * 2. Member는 어떤 파라미터가 넘겨올지 모른다. 개발자 입장에서 API스펙문서를 까보지 않으면 어떤 값들이 넘어오는지 알 수 없다. 하지만 DTO로 받으면 어떤 값만 들어오는지 확인 할 수 있어서 좋다.
     * 3. validation 체크를 DTO에서 해서 엔티티에서 validation을 체크할 때의 문제점들을 해결할 수 있다.
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());
        memberService.join(member);

        return new CreateMemberResponse(member.getId());
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }


}
