package com.waglewagle.server.domain.timeTable.service;

import com.waglewagle.server.domain.timeTable.dto.TimeTableDTO;
import com.waglewagle.server.domain.timeTable.entity.TimeTable;
import com.waglewagle.server.domain.timeTable.repository.TimeTableRepository;
import com.waglewagle.server.global.apiPayload.code.GeneralErrorCode;
import com.waglewagle.server.global.apiPayload.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeTableServiceTest {

    @Mock
    private TimeTableRepository timeTableRepository;

    @InjectMocks
    private TimeTableService timeTableService;

    @Test
    @DisplayName("타임테이블 조회: 정상 조회 성공")
    void getTimeTables_Success() {
        // given
        Long festivalId = 1L;

        TimeTable timeTable = TimeTable.builder()
                .imageUrl("https://s3.image.com/test.jpg")
                .sequence(1)
                .build();

        when(timeTableRepository.findByFestivalIdOrderBySequenceAsc(festivalId))
                .thenReturn(List.of(timeTable));

        // when
        List<TimeTableDTO.TimeTableInfo> result = timeTableService.getTimeTalbes(festivalId);

        // then
        assertEquals(1, result.size());
        assertEquals("https://s3.image.com/test.jpg", result.get(0).imageUrl());
        assertEquals(1, result.get(0).sequence());
    }

    @Test
    @DisplayName("타임테이블 조회: 데이터가 없을 때 TIMETABLE_NOT_FOUND 예외 발생")
    void getTimeTables_NotFound() {
        // given
        Long festivalId = 1L;

        when(timeTableRepository.findByFestivalIdOrderBySequenceAsc(festivalId))
                .thenReturn(Collections.emptyList());

        // when & then
        GeneralException exception = assertThrows(GeneralException.class, () ->
                timeTableService.getTimeTalbes(festivalId)
        );
        assertEquals(GeneralErrorCode.TIMETABLE_NOT_FOUND, exception.getCode());
    }
}
