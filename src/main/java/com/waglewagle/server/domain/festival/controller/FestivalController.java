package com.waglewagle.server.domain.festival.controller;

import com.waglewagle.server.domain.festival.dto.FestivalDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.apiPayload.code.GeneralSuccessCode;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class FestivalController implements FestivalControllerDocs {
    @GetMapping("/api/v1/festivals/recommendations")
    @Override
    public ResponseEntity<ApiResponse<ListResponseDTO<FestivalDTO.FestivalSummary>>> getRecommendedFestivals() {
        return ResponseEntity.ok(ApiResponse.onSuccess(GeneralSuccessCode.OK, null));
    }

    @GetMapping("/api/v1/festivals")
    @Override
    public ResponseEntity<ApiResponse<ListResponseDTO<FestivalDTO.FestivalSummary>>> getFestivals(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.onSuccess(GeneralSuccessCode.OK, null));
    }

    @GetMapping("/api/v1/festivals/{festivalId}")
    @Override
    public ResponseEntity<ApiResponse<FestivalDTO.FestivalDetail>> getFestivalDetail(
            @PathVariable Long festivalId) {
        return ResponseEntity.ok(ApiResponse.onSuccess(GeneralSuccessCode.OK, null));
    }
}
