package com.jwj.community.domain.board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardEditor {

    /**
     * BoardEditor를 추가함으로써 수정하고자 하는 필드만 명시할 수 있다.
     */
    private final String title;
    private final String content;

    @Builder
    public BoardEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
