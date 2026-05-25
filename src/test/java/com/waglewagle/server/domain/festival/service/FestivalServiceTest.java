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
                .posterImageUrl("url")
                .placeName("place")
                .build();
    }

    @Test
    @DisplayName("추천 축제: 진행 중인 축제가 5개 미만일 때 예정된 축제로 채워지는지 확인")
    void getRecommendedFestivals_FillRemainingWithUpcoming() {
        // given
        // 1. 서비스 내부에서 fixedNow를 사용하므로, 테스트에서도 기준 시간을 고정합니다.
        LocalDateTime now = LocalDateTime.now();

        Festival ongoing = createFestival(1L, "진행중 축제", now.minusDays(1), now.plusDays(1));
        Festival upcoming = createFestival(2L, "예정된 축제", now.plusDays(1), now.plusDays(2));

        // 1순위: 진행 중인 축제 1개 반환
        when(festivalRepository.findOngoing(any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(ongoing));

        // 2순위: 남은 4개를 채우기 위해 호출됨 -> 예정된 축제 1개 반환
        when(festivalRepository.findUpcoming(any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(upcoming));

        // 3순위: 그래도 부족해서 호출됨 -> 결과 없음
        when(festivalRepository.findEnd(any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(Collections.emptyList());

        // when
        List<FestivalDTO.FestivalSummary> result = festivalService.getRecommendedFestivals();

        // then
        assertEquals(2, result.size(), "진행중 1개 + 예정 1개 총 2개가 조회되어야 함");

        // 첫 번째 데이터 확인 (ONGOING)
        assertEquals("ONGOING", result.get(0).status());
        assertEquals("진행중 축제", result.get(0).name());

        // 두 번째 데이터 확인 (UPCOMING) - DTO 변환 로직에 따라 상태가 결정됨
        assertEquals("UPCOMING", result.get(1).status());
        assertEquals("예정된 축제", result.get(1).name());

        // 로직 흐름 검증
        verify(festivalRepository).findOngoing(any(), any());
        verify(festivalRepository).findUpcoming(any(), any());
        verify(festivalRepository).findEnd(any(), any()); // 5개가 안 채워졌으므로 결국 마지막까지 호출함
    }

    @Test
    @DisplayName("추천 축제: 데이터가 전혀 없을 때 FESTIVAL_NOT_FOUND 예외 발생 확인")
    void getRecommendedFestivals_ThrowsException() {
        // given
        when(festivalRepository.findOngoing(any(), any())).thenReturn(Collections.emptyList());
        when(festivalRepository.findUpcoming(any(), any())).thenReturn(Collections.emptyList());
        when(festivalRepository.findEnd(any(), any())).thenReturn(Collections.emptyList());

        // when & then
        GeneralException exception = assertThrows(GeneralException.class, () ->
                festivalService.getRecommendedFestivals()
        );
        assertEquals(GeneralErrorCode.FESTIVAL_NOT_FOUND, exception.getCode());
    }

    @Test
    @DisplayName("축제 검색: 검색 결과가 있을 때 페이징된 DTO 리스트를 반환한다")
    void getFestivals_Success() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Festival festival = createFestival(1L, "김천김밥축제", now.plusDays(5), now.plusDays(7));
        Page<Festival> page = new PageImpl<>(List.of(festival));

        when(festivalRepository.searchByKeyword(eq("김밥"), any(Pageable.class)))
                .thenReturn(page);

        // when
        Page<FestivalDTO.FestivalSummary> result = festivalService.getFestivals("김밥", 0, 5);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals("UPCOMING", result.getContent().get(0).status()); // 시작 전이므로 UPCOMING
    }

    @Test
    @DisplayName("상세 조회: 존재하지 않는 ID로 조회 시 FESTIVAL_NOT_FOUND 예외 발생 확인")
    void getFestivalDetail_Fail() {
        // given
        when(festivalRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(GeneralException.class, () -> festivalService.getFestivalDetail(99L));
    }
}