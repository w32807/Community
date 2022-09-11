package com.jwj.community.domain.entity;

import com.jwj.community.web.board.dto.response.BoardResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.min;
import static javax.persistence.GenerationType.AUTO;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String title;

    @Lob
    private String content;

    private int views = 0;

    @ManyToOne
    private Member member;

    @Builder(builderMethodName = "update")
    public Board(Long id, String title, String content){
        this.id = id;
        this.title = title;
        this.content = content;
    }

    @Builder(builderMethodName = "create")
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

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }

    public void addView(){
        this.views = min(++views, MAX_VALUE);
    }

    public BoardResponse toResponse(){
        return BoardResponse.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
