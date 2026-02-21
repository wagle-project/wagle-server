package com.waglewagle.server.domain.timeTable.controller;

import com.waglewagle.server.domain.timeTable.dto.TimeTableDTO;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeTableController implements TimeTableControllerDocs {
    @GetMapping("/api/v1/festivals/{festivalId}/timetables")
    @Override
    public ResponseEntity<ListResponseDTO<TimeTableDTO.TimeTableInfo>> getTimeTables(Long festivalId) {
        return ResponseEntity.ok(null);
    }
}
