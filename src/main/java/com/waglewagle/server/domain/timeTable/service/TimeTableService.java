package com.waglewagle.server.domain.timeTable.service;

import com.waglewagle.server.domain.timeTable.dto.TimeTableDTO;
import com.waglewagle.server.domain.timeTable.entity.TimeTable;
import com.waglewagle.server.domain.timeTable.repository.TimeTableRepository;
import com.waglewagle.server.global.apiPayload.code.GeneralErrorCode;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import com.waglewagle.server.global.apiPayload.exception.GeneralException;
import com.waglewagle.server.global.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;

    public List<TimeTableDTO.TimeTableInfo> getTimeTalbes(
            Long festivalId, CustomUserDetails userDetails) {

        //만약 사용자 동의가 없다면 exception 발생 시킴
        if (!userDetails.getVisitor().getIsTermsAgreed()) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED);
        }

        //festivalId에 맞는 TimeTable을 찾고 sequence 기준으로 오름차순 정렬(여러 개를 찾음)
        List<TimeTable> timeTableList = timeTableRepository.findByFestivalIdOrderBySequenceAsc(festivalId);

        //데이터가 없을 시 exception을 발생 시킴
        if (timeTableList.isEmpty()) {
            throw new GeneralException(GeneralErrorCode.TIMETABLE_NOT_FOUND);
        }

        //DTO로 변환 시킴
        return timeTableList.stream()
                .map(TimeTableDTO.TimeTableInfo::from)
                .toList();
    }
}
