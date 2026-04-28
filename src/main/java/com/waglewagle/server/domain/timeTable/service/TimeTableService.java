package com.waglewagle.server.domain.timeTable.service;

import com.waglewagle.server.domain.timeTable.dto.TimeTableDTO;
import com.waglewagle.server.domain.timeTable.repository.TimeTableRepository;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
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
    }
}
