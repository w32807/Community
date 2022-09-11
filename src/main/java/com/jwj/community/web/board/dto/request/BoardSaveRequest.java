package com.jwj.community.web.board.dto.request;

import com.jwj.community.domain.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardSaveRequest {

    @NotEmpty(message = "{field.required.title}")
    private String title;

    @NotEmpty(message = "{field.required.content}")
    private String content;

    @NotEmpty
    private String email;

    public Board toEntity(){
        return Board.create()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
