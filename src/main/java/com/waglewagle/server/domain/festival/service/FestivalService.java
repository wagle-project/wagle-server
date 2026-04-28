package com.waglewagle.server.domain.festival.service;

import com.waglewagle.server.domain.festival.dto.FestivalDTO;
import com.waglewagle.server.domain.festival.repository.FestivalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private final FestivalRepository festivalRepository;

    public List<FestivalDTO.FestivalSummary> getRecommendedFestivals() {
    }

    public Page<FestivalDTO.FestivalSummary> getFastivals(
            String keyword, int page, int size) {
    }


    public FestivalDTO.FestivalDetail getFestivalDetail(Long festivalId) {
    }
}
