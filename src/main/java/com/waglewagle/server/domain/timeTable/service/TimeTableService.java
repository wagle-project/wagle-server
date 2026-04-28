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

        if (!userDetails.getVisitor().getIsTermsAgreed()) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED);
        }

        List<TimeTable> timeTableList = timeTableRepository.findByFestivalId(festivalId);
        return timeTableList.stream()
                .map(TimeTableDTO.TimeTableInfo::from)
                .toList();
    }
}
