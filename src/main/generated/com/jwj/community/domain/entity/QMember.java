package com.jwj.community.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1293289666L;

    public static final QMember member = new QMember("member1");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.jwj.community.domain.common.enums.Level> level = createEnum("level", com.jwj.community.domain.common.enums.Level.class);

    public final NumberPath<Integer> levelPoint = createNumber("levelPoint", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final SetPath<com.jwj.community.domain.common.enums.Roles, EnumPath<com.jwj.community.domain.common.enums.Roles>> roleSet = this.<com.jwj.community.domain.common.enums.Roles, EnumPath<com.jwj.community.domain.common.enums.Roles>>createSet("roleSet", com.jwj.community.domain.common.enums.Roles.class, EnumPath.class, PathInits.DIRECT2);

    public final EnumPath<com.jwj.community.domain.common.enums.MemberState> state = createEnum("state", com.jwj.community.domain.common.enums.MemberState.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

