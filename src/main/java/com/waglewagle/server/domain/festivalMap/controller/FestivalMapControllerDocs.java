package com.waglewagle.server.domain.festivalMap.controller;

import com.waglewagle.server.domain.festivalMap.dto.FestivalMapDTO;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface FestivalMapControllerDocs {
    @Operation(summary = "축제 지도 오버레이 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "실패")
    })
    @GetMapping("/api/v1/festivals/{festivalId}/maps")
    ResponseEntity<ListResponseDTO<FestivalMapDTO.FestivalMapInfo>> getFestivalMaps(@PathVariable Long festivalId);
}
