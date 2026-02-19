package com.waglewagle.server.domain.timeTable.controller;

import com.waglewagle.server.domain.timeTable.dto.TimeTableDTO;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface TimeTableDocs {

    @Operation(
            summary = "축제 타임테이블 조회",
            description = "축제의 공연 및 이벤트 일정표 이미지 리스트를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/api/v1/festivals/{festivalId}/timetables")
    ResponseEntity<ListResponseDTO<TimeTableDTO.TimeTableInfo>> getTimeTables(
            @Parameter(description = "축제 ID") @PathVariable Long festivalId
    );
}
