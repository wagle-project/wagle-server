package com.waglewagle.server.domain.festival.service;

import com.waglewagle.server.domain.festival.dto.FestivalDTO;
import com.waglewagle.server.domain.festival.entity.Festival;
import com.waglewagle.server.domain.festival.repository.FestivalRepository;
import com.waglewagle.server.global.apiPayload.code.GeneralErrorCode;
import com.waglewagle.server.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        //festivalId에 맞는 festival을 찾음
        Optional<Festival> festivalOptional = festivalRepository.findById(festivalId);

        //값이 없을 시 Exception발생 시키고 값이 있으면 Optional 해제
        Festival festival = festivalOptional.orElseThrow(() ->
                new GeneralException(GeneralErrorCode.NOT_FOUND));

        //DTO로 변환시키고 반환
        return FestivalDTO.FestivalDetail.from(festival);
    }
}
