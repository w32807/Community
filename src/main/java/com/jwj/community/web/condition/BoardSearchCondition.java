package com.jwj.community.web.condition;

import lombok.Data;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Data
public class BoardSearchCondition {

    private static final int MAX_SIZE = 500;

    private final Integer page;
    private final Integer size;

    private String keyword;
    /*
        "T"
        "C"
        "W"
        "TC"
        "CW"
        "TW"
        "TWC"
    * */
    private String searchType;

    public BoardSearchCondition(Integer page, Integer size, String searchType){
        this.page = page == null ? 1 : page;
        this.size = size == null ? 10 : size;
        this.searchType = searchType == null ? "T" : searchType;
    }

    public long getOffset(){
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }

}
