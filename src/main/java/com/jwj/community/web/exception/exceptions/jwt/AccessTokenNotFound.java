package com.jwj.community.web.exception.exceptions.jwt;

import com.jwj.community.web.exception.exceptions.common.CommunityException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class AccessTokenNotFound extends CommunityException {

    public AccessTokenNotFound() {
    }

    public AccessTokenNotFound(String message) {
        super(message);
    }

    public AccessTokenNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getStatusCode() {
        return String.valueOf(NOT_FOUND.value());
    }
}
