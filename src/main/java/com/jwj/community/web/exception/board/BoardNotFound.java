package com.jwj.community.web.exception.board;

import lombok.Data;

@Data
public class BoardNotFound extends RuntimeException{

    public BoardNotFound(String message) {
        super(message);
    }

    public BoardNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
