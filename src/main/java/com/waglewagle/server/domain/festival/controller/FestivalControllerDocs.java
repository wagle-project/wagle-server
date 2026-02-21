package com.waglewagle.server.domain.festival.controller;

import com.waglewagle.server.domain.festival.dto.FestivalDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface FestivalControllerDocs {
    @Operation(summary = "추천 축제 목록 조회 API", description = "메인 화면에 노출될 추천 축제 리스트를 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/api/v1/festivals/recommendations")
    ResponseEntity<ApiResponse<ListResponseDTO<FestivalDTO.FestivalSummary>>> getRecommendedFestivals();

    @Operation(summary = "축제 검색 API", description = "키워드 기반 검색과 페이징된 축제 목록을 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "검색 조건 오류", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/api/v1/festivals")
    ResponseEntity<ApiResponse<ListResponseDTO<FestivalDTO.FestivalSummary>>> getFestivals(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "축제 상세 정보 조회 API", description = "축제의 상세 정보를 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 경로/축제", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/api/v1/festivals/{festivalId}")
    ResponseEntity<ApiResponse<FestivalDTO.FestivalDetail>> getFestivalDetail(
            @PathVariable Long festivalId);
}