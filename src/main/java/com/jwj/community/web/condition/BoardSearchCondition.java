package com.jwj.community.web.condition;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.springframework.data.domain.PageRequest.of;

@Data
public class BoardSearchCondition {

    private static final int MAX_SIZE = 500;

    private final Integer page;
    private final Integer size;
    private String keyword;
    private String searchType;

    public BoardSearchCondition(Integer page, Integer size, String searchType){
        this.page = page == null ? 1 : page;
        this.size = size == null ? 10 : size;
        this.searchType = searchType == null ? "T" : searchType;
    }

    public long getOffset(){
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }

    public Pageable getPageable(){
        return of(max(1, page) - 1, min(size, MAX_SIZE));
    }

}
