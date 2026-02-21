package com.waglewagle.server.domain.festivalMap.controller;

import com.waglewagle.server.domain.festivalMap.dto.FestivalMapDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface FestivalMapControllerDocs {
    @Operation(summary = "축제 지도 오버레이 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/api/v1/festivals/{festivalId}/maps")
    ResponseEntity<ApiResponse<ListResponseDTO<FestivalMapDTO.FestivalMapInfo>>> getFestivalMaps(
            @PathVariable Long festivalId);
}
