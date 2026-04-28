package com.waglewagle.server.domain.festival.controller;

import com.waglewagle.server.domain.festival.dto.FestivalDTO;
import com.waglewagle.server.domain.festival.service.FestivalService;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.apiPayload.code.GeneralSuccessCode;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import com.waglewagle.server.global.apiPayload.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/festivals")
public class FestivalController implements FestivalControllerDocs {

    private final FestivalService festivalService;

    @GetMapping("/recommendations")
    @Override
    public ApiResponse<ListResponseDTO<FestivalDTO.FestivalSummary>> getRecommendedFestivals() {

        List<FestivalDTO.FestivalSummary> result = festivalService.getRecommendedFestivals();

        return ApiResponse.onListSuccess(GeneralSuccessCode.OK, result);
    }

    @GetMapping("")
    @Override
    public ApiResponse<PageResponseDTO<FestivalDTO.FestivalSummary>> getFestivals(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<FestivalDTO.FestivalSummary> result = festivalService
                .getFastivals(keyword, page, size);

        return ApiResponse.onPageSuccess(GeneralSuccessCode.OK, result);
    }

    @GetMapping("/{festivalId}")
    @Override
    public ApiResponse<FestivalDTO.FestivalDetail> getFestivalDetail(
            @PathVariable Long festivalId) {

        FestivalDTO.FestivalDetail result = festivalService
                .getFestivalDetail(festivalId);

        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }
}
