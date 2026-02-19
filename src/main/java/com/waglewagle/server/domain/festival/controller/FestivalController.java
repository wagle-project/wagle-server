package com.waglewagle.server.domain.festival.controller;

import com.waglewagle.server.domain.festival.dto.FestivalDTO;
import com.waglewagle.server.global.apiPayload.code.GeneralSuccessCode;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FestivalController implements FestivalDocs {

    private final FestivalService festivalService;

    @Override
    public ResponseEntity<ListResponseDTO<FestivalDTO.FestivalSummary>> getRecommendedFestivals() {
        List<FestivalDTO.FestivalSummary> summaries = festivalService.getTop5Recommended();

        return ResponseEntity
                .status(GeneralSuccessCode.OK.getStatus())
                .body(ListResponseDTO.of(summaries));
    }

    @Override
    public ResponseEntity<ListResponseDTO<FestivalDTO.FestivalSummary>> getFestivals(
            String keyword, int page, int size
    ) {
        List<FestivalDTO.FestivalSummary> summaries = festivalService.searchFestivals(keyword, page, size);

        return ResponseEntity
                .status(GeneralSuccessCode.OK.getStatus())
                .body(ListResponseDTO.of(summaries));
    }

    @Override
    public ResponseEntity<FestivalDTO.FestivalDetail> getFestivalDetail(Long festivalId) {
        FestivalDTO.FestivalDetail detail = festivalService.getFestivalDetail(festivalId);

        return ResponseEntity
                .status(GeneralSuccessCode.OK.getStatus())
                .body(detail);
    }
}
