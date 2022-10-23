package com.jwj.community.web.member.controller;

import com.jwj.community.domain.member.service.MemberService;
import com.jwj.community.web.common.result.ListResult;
import com.jwj.community.web.common.result.Result;
import com.jwj.community.web.member.dto.request.MemberSaveRequest;
import com.jwj.community.web.member.dto.request.MemberUpdateRequest;
import com.jwj.community.web.member.dto.response.MemberResponse;
import com.jwj.community.web.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberRestController {

    private final MemberService memberService;
    private final MemberValidator memberValidator;

    @GetMapping("/members")
    public ResponseEntity<ListResult<MemberResponse>> members(){
        // todo Pageable로 페이징 기능 구현해야 됨
        List<MemberResponse> members = memberService.getMembers().stream()
                .map(member -> member.toResponse())
                .collect(toList());

        ListResult<MemberResponse> listResult = ListResult.<MemberResponse>builder()
                .list(members)
                .build();

        return ok().body(listResult);
    }

    @GetMapping("/member")
    public ResponseEntity<Result> member(@PathVariable("id") Long id){
        MemberResponse member = memberService.findById(id).toResponse();

        Result<MemberResponse> result = Result.<MemberResponse>builder()
                .data(member)
                .build();

        return ok().body(result);
    }

    @PostMapping("/addMember")
    public ResponseEntity<Result> addMember(@RequestBody @Valid MemberSaveRequest request, BindingResult bindingResult) throws BindException {
        memberValidator.validate(request, bindingResult);

        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        Result<Long> result = Result.<Long>builder()
                .data(memberService.addMember(request.toEntity()))
                .build();

        return ok().body(result);
    }

    @PutMapping("/member")
    public ResponseEntity<Result> updateBoard(@RequestBody @Valid MemberUpdateRequest request){
        // todo 나중에 변경할 항목을 정해서 구현
        return null;
    }

    @PutMapping("/deActive")
    public void deActiveMember(@PathVariable("id") Long id){
        // todo 회원 삭제 대신 활성화를 비활성화로 바꾸는 기능 구현하기
        memberService.deActiveMember(id);
    }

}
