package com.jwj.community.domain.entity;

import com.jwj.community.domain.common.enums.Level;
import com.jwj.community.domain.common.enums.MemberState;
import com.jwj.community.domain.common.enums.Roles;
import com.jwj.community.utils.ModelMapperUtils;
import com.jwj.community.web.login.request.MemberSaveRequest;
import com.jwj.community.web.login.response.MemberResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static com.jwj.community.domain.common.enums.Level.LEVEL1;
import static com.jwj.community.domain.common.enums.MemberState.ACTIVE;
import static com.jwj.community.domain.common.enums.MemberState.DEACTIVE;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private int levelPoint = 0;
    private Level level = LEVEL1;
    private MemberState state = ACTIVE;

    public Member(MemberSaveRequest request) {
        this.email = request.getEmail();
        this.password = request.getPassword();
        this.nickname = request.getNickname();
    }

    @ElementCollection(fetch = FetchType.EAGER) // Enum의 컬렉션 객체임을 알려주는 Annotation
    @Enumerated(EnumType.STRING) // 기본으로 Enum 값이 컬럼에 들어가면 선언된 순서인 정수값이 들어간다.
    private final Set<Roles> roleSet = new HashSet<>();

    public void addRole(Roles roles) {
        roleSet.add(roles);
    }

    public void changeState(){
        this.state = (this.state == ACTIVE) ? DEACTIVE : ACTIVE;
    }

    public void addLevelPoint(){
        levelPoint = (levelPoint == Integer.MAX_VALUE) ? Integer.MAX_VALUE : levelPoint + 1;
    }

    public void updateLevel(){
        this.level = Level.findLevel(this.levelPoint);
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public MemberResponse toResponse(){
        ModelMapperUtils mapper = new ModelMapperUtils();
        return mapper.map(this, MemberResponse.class);
    }

}
