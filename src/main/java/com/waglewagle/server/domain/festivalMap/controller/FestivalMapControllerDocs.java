package com.waglewagle.server.domain.festivalMap.controller;

import com.waglewagle.server.domain.festivalMap.dto.FestivalMapDTO;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface FestivalMapControllerDocs {

    @Operation(
            summary = "축제 지도 오버레이 조회",
            description = "특정 축제에 등록된 지도 이미지와 좌표 경계(Bounds) 리스트를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/api/v1/festivals/{festivalId}/maps")
    ResponseEntity<ListResponseDTO<FestivalMapDTO.FestivalMapInfo>> getFestivalMaps(
            @Parameter(description = "축제 ID") @PathVariable Long festivalId
    );
}
