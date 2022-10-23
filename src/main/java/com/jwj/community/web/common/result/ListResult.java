package com.jwj.community.web.common.result;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class ListResult<T> {

    public List<T> list;
    public int size;

    public long pageNumber;
    public long pageSize;
    public long totalPage;
    public long totalSize;

    @Builder
    public ListResult(List<T> list, Page page) {
        this.list = list;
        this.size = list.size();
        this.pageNumber = page.getPageable().getPageNumber();
        this.pageSize = page.getPageable().getPageSize();
        this.totalPage = page.getTotalPages();
        this.totalSize = page.getTotalElements();
    }

    @Builder
    public ListResult(List<T> list) {
        this.list = list;
        this.size = list.size();
    }
}
