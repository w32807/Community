package com.jwj.community.web.common.result;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ListResult<T> {

    public List<T> list;
    public int size;
    public long totalSize;

    @Builder
    public ListResult(List<T> list, long totalSize) {
        this.list = list;
        this.size = list.size();
        this.totalSize = totalSize;
    }

    @Builder
    public ListResult(List<T> list) {
        this.list = list;
        this.size = list.size();
    }
}
