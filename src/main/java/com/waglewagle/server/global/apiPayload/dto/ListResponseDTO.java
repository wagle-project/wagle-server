package com.waglewagle.server.global.apiPayload.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class ListResponseDTO<T> {

    private final List<T> content;
    private final int totalElements;

    public ListResponseDTO(List<T> content) {
        this.content = content;
        this.totalElements = content.size();
    }

    // (선택 사항) static 팩토리 메서드 - 코드를 좀 더 예쁘게 쓰고 싶다면 추가
    public static <T> ListResponseDTO<T> of(List<T> content) {
        return new ListResponseDTO<>(content);
    }
}