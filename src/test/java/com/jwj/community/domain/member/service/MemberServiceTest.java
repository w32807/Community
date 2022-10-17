package com.jwj.community.domain.member.service;

import com.jwj.community.domain.entity.Member;
import com.jwj.community.web.member.dto.request.MemberSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static com.jwj.community.domain.common.enums.Level.LEVEL1;
import static com.jwj.community.domain.common.enums.MemberState.ACTIVE;
import static com.jwj.community.domain.common.enums.MemberState.DEACTIVE;
import static com.jwj.community.domain.common.enums.Roles.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원 등록하기")
    void test1() throws Exception{
        // given
        MemberSaveRequest request = MemberSaveRequest.builder()
                .email("test@google.com")
                .password("1234")
                .nickname("test nickname")
                .build();

        // when
        Long savedMemberId = memberService.addMember(request.toEntity());
        Member saveMember = memberService.findById(savedMemberId);

        // then
        assertThat(saveMember.getId()).isEqualTo(savedMemberId);
        assertThat(saveMember.getEmail()).isEqualTo("test@google.com");
        assertThat(saveMember.getNickname()).isEqualTo("test nickname");
        assertThat(saveMember.getLevelPoint()).isEqualTo(0);
        assertThat(saveMember.getLevel()).isEqualTo(LEVEL1);
        assertThat(saveMember.getState()).isEqualTo(ACTIVE);
        assertThat(saveMember.getRoleSet()).contains(ROLE_MEMBER);
    }

    @Test
    @DisplayName("회원 비활성화하기")
    void test2() throws Exception{
        // given
        MemberSaveRequest request = MemberSaveRequest.builder()
                .email("test@google.com")
                .password("1234")
                .nickname("test nickname")
                .build();

        // when
        Long savedMemberId = memberService.addMember(request.toEntity());
        memberService.deActiveMember(savedMemberId);
        Member saveMember = memberService.findById(savedMemberId);

        // then
        assertThat(saveMember.getState()).isEqualTo(DEACTIVE);
    }

}