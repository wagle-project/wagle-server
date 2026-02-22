package com.waglewagle.server.domain.festival.controller;

import com.waglewagle.server.domain.festival.dto.FestivalDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.apiPayload.code.GeneralSuccessCode;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import com.waglewagle.server.global.apiPayload.dto.PageResponseDTO;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/festivals")
public class FestivalController implements FestivalControllerDocs {
    @GetMapping("/recommendations")
    @Override
    public ApiResponse<ListResponseDTO<FestivalDTO.FestivalSummary>> getRecommendedFestivals() {
        return ApiResponse.onListSuccess(GeneralSuccessCode.OK, null);
    }

    @GetMapping("")
    @Override
    public ApiResponse<PageResponseDTO<FestivalDTO.FestivalSummary>> getFestivals(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onPageSuccess(GeneralSuccessCode.OK, null);
    }

    @GetMapping("/{festivalId}")
    @Override
    public ApiResponse<FestivalDTO.FestivalDetail> getFestivalDetail(
            @PathVariable Long festivalId) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
    }
}
