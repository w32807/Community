package com.jwj.community.web.board.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardResponse {

    private Long id;
    private String title;
    private String content;

}
