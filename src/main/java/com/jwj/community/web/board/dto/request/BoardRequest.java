package com.jwj.community.web.board.dto.request;

import com.jwj.community.domain.entity.Board;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class BoardRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    public Board toEntity(){
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
