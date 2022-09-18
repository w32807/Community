package com.jwj.community.domain.entity;

import com.jwj.community.web.login.request.RefreshTokenRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;

    @OneToOne(mappedBy = "refreshToken")
    private Member member;

    public RefreshToken(RefreshTokenRequest request){
        this.token = request.getRefreshToken();
    }

    public void changeRefreshToken(String refreshToken){
        this.token = refreshToken;
    }

    public void setMember(Member member){
        this.member = member;
    }

}
