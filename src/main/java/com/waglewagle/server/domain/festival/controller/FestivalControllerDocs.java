package com.waglewagle.server.domain.festival.controller;

import com.waglewagle.server.domain.festival.dto.FestivalDTO;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface FestivalControllerDocs {
    @Operation(summary = "추천 축제 목록 조회 API", description = "상위 5개 추천 축제 리스트를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "실패")
    })
    @GetMapping("/api/v1/festivals/recommendations")
    ResponseEntity<ListResponseDTO<FestivalDTO.FestivalSummary>> getRecommendedFestivals();

    @Operation(summary = "축제 검색 및 목록 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "실패")
    })
    @GetMapping("/api/v1/festivals")
    ResponseEntity<ListResponseDTO<FestivalDTO.FestivalSummary>> getFestivals(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "축제 상세 정보 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "실패")
    })
    @GetMapping("/api/v1/festivals/{festivalId}")
    ResponseEntity<FestivalDTO.FestivalDetail> getFestivalDetail(@PathVariable Long festivalId);
}