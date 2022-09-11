package com.jwj.community.web.exception.exceptions.common;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class CommunityException extends RuntimeException{

    public CommunityException(String message) {
        super(message);
    }

    public CommunityException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String getStatusCode();

}
