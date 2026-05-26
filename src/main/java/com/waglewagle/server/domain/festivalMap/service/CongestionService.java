package com.waglewagle.server.domain.festivalMap.service;

import com.waglewagle.server.domain.festivalMap.dto.CongestionDTO;
import com.waglewagle.server.domain.festivalMap.repository.FestivalMapRepository;
import com.waglewagle.server.global.apiPayload.code.GeneralErrorCode;
import com.waglewagle.server.global.apiPayload.exception.GeneralException;
import com.waglewagle.server.global.redis.repository.LocationRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CongestionService {

    private final FestivalMapRepository festivalMapRepository;
    private final LocationRedisRepository locationRedisRepository;

    public CongestionDTO.CongestionResponse getCongestion(Long mapId) {
        // 맵 존재 여부 확인
        festivalMapRepository.findById(mapId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.MAP_NOT_FOUND));

        Map<String, Integer> cellCounts = locationRedisRepository.getCellCounts(mapId);

        int totalCount = cellCounts.values().stream().mapToInt(Integer::intValue).sum();

        List<CongestionDTO.ZoneInfo> zones = cellCounts.entrySet().stream()
                .map(e -> new CongestionDTO.ZoneInfo(
                        e.getKey(),
                        e.getValue(),
                        calculateLevel(e.getValue())
                ))
                .toList();

        return new CongestionDTO.CongestionResponse(
                System.currentTimeMillis(),
                totalCount,
                zones
        );
    }

    // 혼잡도 4단계 (셀당 절대 인원 기준)
    private int calculateLevel(int count) {
        if (count >= 3) return 4;
        if (count >= 2) return 3;
        return 2;
    }
}