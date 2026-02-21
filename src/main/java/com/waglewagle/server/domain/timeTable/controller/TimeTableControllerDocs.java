package com.waglewagle.server.domain.timeTable.controller;

import com.waglewagle.server.domain.timeTable.dto.TimeTableDTO;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface TimeTableControllerDocs {
    @Operation(summary = "축제 타임테이블 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "실패")
    })
    @GetMapping("/api/v1/festivals/{festivalId}/timetables")
    ResponseEntity<ListResponseDTO<TimeTableDTO.TimeTableInfo>> getTimeTables(@PathVariable Long festivalId);
}
