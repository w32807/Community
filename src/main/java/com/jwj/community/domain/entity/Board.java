package com.jwj.community.domain.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.min;

@Entity
@Getter
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Lob
    private String content;

    private int views = 0;

    @ManyToOne
    private Member member;

    @Builder
    public Board(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void setMember(Member member){
        // 무한 루프에 빠지지 않도록 처리
        if(!member.getBoards().contains(this)){
            member.getBoards().add(this);
        }
    }

    public void addView(){
        this.views = min(++views, MAX_VALUE);
    }
}
