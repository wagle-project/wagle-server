package com.waglewagle.server.domain.timeTable.controller;

import com.waglewagle.server.domain.timeTable.dto.TimeTableDTO;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TimeTableController implements TimeTableDocs {

    private final TimeTableService timeTableService;

    @Override
    public ResponseEntity<ListResponseDTO<TimeTableDTO.TimeTableInfo>> getTimeTables(Long festivalId) {
        return ResponseEntity.ok(ListResponseDTO.of(timeTableService.getTimeTablesByFestival(festivalId)));
    }
}
