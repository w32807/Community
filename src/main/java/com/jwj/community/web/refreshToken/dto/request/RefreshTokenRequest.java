package com.jwj.community.web.refreshToken.dto.request;

import com.jwj.community.domain.entity.RefreshToken;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenRequest {

    private String refreshToken;

    public RefreshToken toEntity(){
        return new RefreshToken(this);
    }

}
