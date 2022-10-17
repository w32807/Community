package com.jwj.community.config.init;

import com.jwj.community.domain.member.service.MemberService;
import com.jwj.community.utils.JSONReadUtils;
import com.jwj.community.web.member.dto.request.MemberSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
public class initService {

    private final MemberService memberService;

    @PostConstruct
    public void memberInit(){
        String filePath = "initData/memberInitData.json";

        List<MemberSaveRequest> list =  JSONReadUtils.readJSONList(filePath, MemberSaveRequest.class);

        list.stream().forEach(member -> memberService.addMember(member.toEntity()));
    }
}
