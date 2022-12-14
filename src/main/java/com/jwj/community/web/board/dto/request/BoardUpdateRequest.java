package com.jwj.community.web.board.dto.request;

import com.jwj.community.domain.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateRequest {

    @NotNull
    private Long id;

    @NotBlank(message = "{field.required.title}")
    private String title;

    @NotBlank(message = "{field.required.content}")
    private String content;

    public Board toEntity(){
        return Board.update()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
