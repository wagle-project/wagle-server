package com.waglewagle.server.domain.festivalMap.service;

import com.waglewagle.server.domain.festival.repository.FestivalRepository;
import com.waglewagle.server.domain.festivalMap.dto.FestivalMapDTO;
import com.waglewagle.server.domain.festivalMap.repository.FestivalMapRepository;
import com.waglewagle.server.global.apiPayload.code.GeneralErrorCode;
import com.waglewagle.server.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FestivalMapService {

    private final FestivalRepository festivalRepository;
    private final FestivalMapRepository festivalMapRepository;

    public List<FestivalMapDTO.FestivalMapInfo> getFestivalMaps(Long festivalId) {
        if (!festivalRepository.existsById(festivalId)) {
            throw new GeneralException(GeneralErrorCode.FESTIVAL_NOT_FOUND);
        }

        return festivalMapRepository.findByFestivalIdOrderBySequenceAsc(festivalId)
                .stream()
                .map(FestivalMapDTO.FestivalMapInfo::from)
                .toList();
    }
}
