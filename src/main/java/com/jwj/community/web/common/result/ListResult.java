package com.jwj.community.web.common.result;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ListResult<T> {

    public List<T> list;
    public int size;

    @Builder
    public ListResult(List<T> list) {
        this.list = list;
        this.size = list.size();
    }
}
