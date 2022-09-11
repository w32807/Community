package com.jwj.community.web.exception.exceptions.board;

import com.jwj.community.web.exception.exceptions.common.CommunityException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class BoardNotFound extends CommunityException {

    public BoardNotFound(String message) {
        super(message);
    }

    public BoardNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getStatusCode() {
        return String.valueOf(NOT_FOUND.value());
    }
}
