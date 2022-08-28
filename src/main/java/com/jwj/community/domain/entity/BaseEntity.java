package com.jwj.community.domain.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass // @MappedSuperclass를 선언하여 DB의 테이블로 만들지 않도록 설정
@EntityListeners(value = {AuditingEntityListener.class})// 엔티티가 변경되는 것을 감지하는 리스너
@Getter
public abstract class BaseEntity {

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime regDate;
	
	@LastModifiedDate
	private LocalDateTime modDate;

}

/*
	AuditingEntityListener의 엔티티가 변경됨을 감지하는 기능을 이용해, regDate, modDate를 자동으로 관리하자.
	spring-boot에서는 기본적으로 jar 파일이 import 되어 있었지만, spring mvc 에서는 spring-aspects를 pom에 추가해야 한다.
	이렇게 되면 DB에 default값 혹은 따로 서비스에서 등록시간, 수정시간을 관리하지 않아도 AuditingEntityListener가 관리한다.
	그리고 RootConfig에 @EnableJpaAuditing를 선언해주어야 사용가능하다.
 */