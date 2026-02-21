package com.waglewagle.server.domain.festival.controller;

import com.waglewagle.server.domain.festival.dto.FestivalDTO;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class FestivalController implements FestivalControllerDocs {
    @GetMapping("/api/v1/festivals/recommendations")
    @Override
    public ResponseEntity<ListResponseDTO<FestivalDTO.FestivalSummary>> getRecommendedFestivals() {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/api/v1/festivals")
    @Override
    public ResponseEntity<ListResponseDTO<FestivalDTO.FestivalSummary>> getFestivals(String keyword, int page, int size) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/api/v1/festivals/{festivalId}")
    @Override
    public ResponseEntity<FestivalDTO.FestivalDetail> getFestivalDetail(@PathVariable Long festivalId) {
        return ResponseEntity.ok(null);
    }
}
