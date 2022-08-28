package com.jwj.community.utils;

import com.jwj.community.web.login.request.MemberSaveRequest;
import com.jwj.community.web.login.response.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ModelMapperUtilsTest {

    private ModelMapperUtils modelMapper = new ModelMapperUtils();

    @Test
    @DisplayName("ModelMapper 변환 테스트 - entity에서 response")
    public void test1() throws Exception{
        // given
        MemberSaveRequest request = MemberSaveRequest.builder()
                .email("admin@google.com")
                .password("1234")
                .nickname("관리자")
                .build();
        // when
        MemberResponse member = modelMapper.map(request.toEntity(), MemberResponse.class);
        // then
        assertThat(member.getEmail()).isEqualTo(request.getEmail());
        assertThat(member.getPassword()).isEqualTo(request.getPassword());
        assertThat(member.getNickname()).isEqualTo(request.getNickname());
    }

}