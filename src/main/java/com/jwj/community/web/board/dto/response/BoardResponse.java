package com.jwj.community.web.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jwj.community.domain.entity.Board;
import com.jwj.community.web.member.dto.response.WriteMemberResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardResponse {

    private Long id;
    private String title;
    private String content;
    private WriteMemberResponse member;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;

    public static BoardResponse of(Board board){
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .regDate(board.getModDate())
                .build();
    }
}
