package com.jwj.community.web.exception.exceptions.refreshToken;

import com.jwj.community.web.exception.exceptions.common.CommunityException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class RefreshTokenNotFound extends CommunityException {

    public RefreshTokenNotFound(String message) {
        super(message);
    }

    public RefreshTokenNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getStatusCode() {
        return String.valueOf(NOT_FOUND.value());
    }
}
