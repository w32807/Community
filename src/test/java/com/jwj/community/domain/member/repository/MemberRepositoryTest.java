package com.jwj.community.domain.member.repository;

import com.jwj.community.domain.entity.Member;
import com.jwj.community.web.member.dto.request.MemberSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setup(){
        MemberSaveRequest request = MemberSaveRequest.builder()
                .email("admin@google.com")
                .password("1234")
                .nickname("닉네임")
                .build();

        memberRepository.save(request.toEntity());
    }

    @Test
    @DisplayName("이메일로 회원 조회하기")
    public void test1() throws Exception{
        // given
        String email = "admin@google.com";
        // when
        Member member = memberRepository.findByEmail(email);
        // then
        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("이메일로 회원 조회했을 떄 없으면 null 반환")
    public void test2() throws Exception{
        // given
        String email = "존재하지 않는 이메일";
        // when
        Member member = memberRepository.findByEmail(email);
        // then
        assertThat(member).isNull();
    }

}