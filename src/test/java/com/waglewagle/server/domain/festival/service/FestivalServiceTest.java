package com.waglewagle.server.domain.festival.service;

import com.waglewagle.server.domain.festival.dto.FestivalDTO;
import com.waglewagle.server.domain.festival.entity.Festival;
import com.waglewagle.server.domain.festival.repository.FestivalRepository;
import com.waglewagle.server.global.apiPayload.code.GeneralErrorCode;
import com.waglewagle.server.global.apiPayload.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FestivalServiceTest {

    @Mock
    private FestivalRepository festivalRepository;

    @InjectMocks
    private FestivalService festivalService;

    // 테스트용 헬퍼 메서드: 반복되는 Festival 객체 생성을 줄여줍니다.
    private Festival createFestival(Long id, String name, LocalDateTime start, LocalDateTime end) {
        return Festival.builder()
                .id(id)
                .name(name)
                .startDate(start)
                .endDate(end)
                .posterUrl("url")
                .placeName("place")
                .build();
    }

    @Test
    @DisplayName("추천 축제: 진행 중인 축제가 있을 때 DTO의 상태가 ONGOING으로 변환되는지 확인")
    void getRecommendedFestivals_OngoingSuccess() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Festival ongoing = createFestival(1L, "진행중 축제", now.minusDays(1), now.plusDays(1));

        when(festivalRepository.findOngoing(any(), any(Pageable.class)))
                .thenReturn(List.of(ongoing));

        // when
        List<FestivalDTO.FestivalSummary> result = festivalService.getRecommendedFestivals();

        // then
        assertFalse(result.isEmpty());
        assertEquals("ONGOING", result.get(0).status());
        assertEquals("진행중 축제", result.get(0).name());
        verify(festivalRepository, times(1)).findOngoing(any(), any());
        verify(festivalRepository, never()).findUpcoming(any(), any()); // 1순위에서 끝났으므로 2순위는 호출 안 됨
    }

    @Test
    @DisplayName("추천 축제: 데이터가 전혀 없을 때 NOT_FOUND 예외 발생 확인")
    void getRecommendedFestivals_ThrowsException() {
        // given
        when(festivalRepository.findOngoing(any(), any())).thenReturn(Collections.emptyList());
        when(festivalRepository.findUpcoming(any(), any())).thenReturn(Collections.emptyList());
        when(festivalRepository.findEnd(any(), any())).thenReturn(Collections.emptyList());

        // when & then
        GeneralException exception = assertThrows(GeneralException.class, () ->
                festivalService.getRecommendedFestivals()
        );
        assertEquals(GeneralErrorCode.NOT_FOUND, exception.getCode());
    }

    @Test
    @DisplayName("축제 검색: 검색 결과가 있을 때 페이징된 DTO 리스트를 반환한다")
    void getFestivals_Success() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Festival festival = createFestival(1L, "김천김밥축제", now.plusDays(5), now.plusDays(7));
        Page<Festival> page = new PageImpl<>(List.of(festival));

        when(festivalRepository.findByNameContaining(eq("김밥"), any(Pageable.class)))
                .thenReturn(page);

        // when
        Page<FestivalDTO.FestivalSummary> result = festivalService.getFastivals("김밥", 0, 5);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals("UPCOMING", result.getContent().get(0).status()); // 시작 전이므로 UPCOMING
    }

    @Test
    @DisplayName("상세 조회: 존재하지 않는 ID로 조회 시 NOT_FOUND 예외 발생 확인")
    void getFestivalDetail_Fail() {
        // given
        when(festivalRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(GeneralException.class, () -> festivalService.getFestivalDetail(99L));
    }
}