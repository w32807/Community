package com.jwj.community.domain.entity;

import com.jwj.community.domain.board.dto.BoardEditor;
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

    private Integer views = 0;

    @ManyToOne(fetch = FetchType.EAGER)
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
        this.member = member;
        // 무한 루프에 빠지지 않도록 처리
        if(!member.getBoards().contains(this)){
            member.getBoards().add(this);
        }
    }

    public BoardEditor.BoardEditorBuilder toEditor(){
        return BoardEditor.builder()
                .title(this.title)
                .content(this.content);
    }

    public void edit(BoardEditor boardEditor){
        this.title = boardEditor.getTitle();
        this.content = boardEditor.getContent();
    }

    public void addView(){
        this.views = min(++views, MAX_VALUE);
    }

}
