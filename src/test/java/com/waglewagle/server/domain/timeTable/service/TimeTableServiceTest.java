package com.waglewagle.server.domain.timeTable.service;

import com.waglewagle.server.domain.timeTable.dto.TimeTableDTO;
import com.waglewagle.server.domain.timeTable.entity.TimeTable;
import com.waglewagle.server.domain.timeTable.repository.TimeTableRepository;
import com.waglewagle.server.domain.visitor.entity.Visitor; // Visitor 엔티티 패키지 경로 확인 필요
import com.waglewagle.server.global.apiPayload.code.GeneralErrorCode;
import com.waglewagle.server.global.apiPayload.exception.GeneralException;
import com.waglewagle.server.global.security.userdetails.CustomUserDetails;
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

    @Mock
    private CustomUserDetails userDetails;

    @Mock
    private Visitor visitor; // UserDetails 내부의 Visitor도 Mocking

    @InjectMocks
    private TimeTableService timeTableService;

    @Test
    @DisplayName("타임테이블 조회: 약관 동의한 사용자가 조회 시 성공")
    void getTimeTables_Success() {
        // given
        Long festivalId = 1L;

        // Mock 설정: userDetails.getVisitor().getIsTermsAgreed() -> true
        when(userDetails.getVisitor()).thenReturn(visitor);
        when(visitor.getIsTermsAgreed()).thenReturn(true);

        TimeTable timeTable = TimeTable.builder()
                .imageUrl("https://s3.image.com/test.jpg")
                .sequence(1)
                .build();

        when(timeTableRepository.findByFestivalIdOrderBySequenceAsc(festivalId))
                .thenReturn(List.of(timeTable));

        // when
        List<TimeTableDTO.TimeTableInfo> result = timeTableService.getTimeTalbes(festivalId, userDetails);

        // then
        assertEquals(1, result.size());
        assertEquals("https://s3.image.com/test.jpg", result.get(0).imageUrl());
        assertEquals(1, result.get(0).sequence());
    }

    @Test
    @DisplayName("타임테이블 조회: 약관 미동의 사용자가 조회 시 UNAUTHORIZED 예외 발생")
    void getTimeTables_UnAuthorized() {
        // given
        Long festivalId = 1L;

        // Mock 설정: userDetails.getVisitor().getIsTermsAgreed() -> false
        when(userDetails.getVisitor()).thenReturn(visitor);
        when(visitor.getIsTermsAgreed()).thenReturn(false);

        // when & then
        GeneralException exception = assertThrows(GeneralException.class, () ->
                timeTableService.getTimeTalbes(festivalId, userDetails)
        );
        assertEquals(GeneralErrorCode.UNAUTHORIZED, exception.getCode());

        // 예외가 발생했으므로 레포지토리는 호출되지 않아야 함
        verify(timeTableRepository, never()).findByFestivalIdOrderBySequenceAsc(anyLong());
    }

    @Test
    @DisplayName("타임테이블 조회: 데이터가 없을 때 NOT_FOUND 예외 발생")
    void getTimeTables_NotFound() {
        // given
        Long festivalId = 1L;
        when(userDetails.getVisitor()).thenReturn(visitor);
        when(visitor.getIsTermsAgreed()).thenReturn(true);

        when(timeTableRepository.findByFestivalIdOrderBySequenceAsc(festivalId))
                .thenReturn(Collections.emptyList());

        // when & then
        GeneralException exception = assertThrows(GeneralException.class, () ->
                timeTableService.getTimeTalbes(festivalId, userDetails)
        );
        assertEquals(GeneralErrorCode.NOT_FOUND, exception.getCode());
    }
}
