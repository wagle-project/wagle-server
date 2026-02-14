package com.waglewagle.server.global.apiPayload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponseDTO<T> {
    private List<T> content;
    private int totalPage;
    private long totalElements;
    private boolean isFirst;
    private boolean isLast;
    private int listSize;
    private int currentPage;

    public PageResponseDTO(Page<T> page) {
        this.content = page.getContent();
        this.totalPage = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
        this.listSize = page.getSize();
        this.currentPage = page.getNumber() + 1;
    }
}