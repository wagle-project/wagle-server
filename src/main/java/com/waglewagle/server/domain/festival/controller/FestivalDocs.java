package com.waglewagle.server.domain.festival.controller;

import com.waglewagle.server.domain.festival.dto.FestivalDTO;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface FestivalDocs {

    @Operation(summary = "추천 축제 목록 조회 (5개)", description = "메인 홈 화면용 상위 5개 추천 축제 리스트를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @GetMapping("/api/v1/festivals/recommendations")
    ResponseEntity<ListResponseDTO<FestivalDTO.FestivalSummary>> getRecommendedFestivals();

    @Operation(summary = "축제 검색 및 목록 조회", description = "키워드 검색 및 페이징된 축제 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/api/v1/festivals")
    ResponseEntity<ListResponseDTO<FestivalDTO.FestivalSummary>> getFestivals(
            @Parameter(description = "검색어") @RequestParam(required = false) String keyword,
            @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지당 개수") @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "축제 상세 조회", description = "특정 축제의 모든 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "축제를 찾을 수 없음")
    })
    @GetMapping("/api/v1/festivals/{festivalId}")
    ResponseEntity<FestivalDTO.FestivalDetail> getFestivalDetail(
            @Parameter(description = "축제 ID") @PathVariable Long festivalId
    );
}