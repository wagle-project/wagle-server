package com.waglewagle.server.domain.visitor.repository;

import com.uber.h3core.H3Core;
import com.waglewagle.server.domain.festivalMap.entity.FestivalMap;
import com.waglewagle.server.domain.festivalMap.repository.FestivalMapRepository;
import com.waglewagle.server.domain.visitor.dto.VisitorLocationDTO;
import com.waglewagle.server.global.redis.repository.LocationRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorLocationService {

    private final FestivalMapRepository festivalMapRepository;
    private final LocationRedisRepository locationRedisRepository;

    private static final H3Core h3 = createH3();
    private static final int H3_RESOLUTION = 14;
    private static final long LOCATION_UPDATE_INTERVAL = 3000L;

    private static H3Core createH3() {
        try { return H3Core.newInstance(); }
        catch (Exception e) { throw new RuntimeException("H3 초기화 실패", e); }
    }

    public VisitorLocationDTO.LocationUpdateResponse updateLocation(
            Long festivalId, String uuid,
            VisitorLocationDTO.LocationUpdateRequest request) {

        double lat = request.lat();
        double lng = request.lng();

        // 1. 축제에 속한 맵 목록 조회
        List<FestivalMap> maps = festivalMapRepository.findByFestivalId(festivalId);

        // 2. 현재 좌표가 속하는 맵 탐색 (직사각형 바운딩 박스 기준)
        FestivalMap currentMap = maps.stream()
                .filter(m -> isInsideMap(m, lat, lng))
                .findFirst()
                .orElse(null);

        boolean isInside = currentMap != null;
        Long currentMapId = isInside ? currentMap.getId() : null;
        String h3Index = isInside ? h3.latLngToCellAddress(lat, lng, H3_RESOLUTION) : null;

        // 3. 이전 위치 Redis에서 조회 후 카운트 감소
        locationRedisRepository.getVisitorLocation(uuid).ifPresent(prev -> {
            String[] parts = prev.split(":");
            if (parts.length == 2) {
                Long prevMapId = Long.parseLong(parts[0]);
                String prevH3 = parts[1];
                locationRedisRepository.decrementCell(prevMapId, prevH3);
            }
        });

        // 4. 현재 위치 저장 및 카운트 증가
        if (isInside) {
            locationRedisRepository.saveVisitorLocation(uuid, currentMapId, h3Index);
            locationRedisRepository.incrementCell(currentMapId, h3Index);
        } else {
            locationRedisRepository.deleteVisitorLocation(uuid);
        }

        return new VisitorLocationDTO.LocationUpdateResponse(
                isInside, currentMapId, LOCATION_UPDATE_INTERVAL);
    }

    // 직사각형 바운딩 박스 내부 여부 판별
    private boolean isInsideMap(FestivalMap map, double lat, double lng) {
        if (map.getSouthWestLat() == null) return false;
        return lat >= map.getSouthWestLat() && lat <= map.getNorthEastLat()
                && lng >= map.getSouthWestLon() && lng <= map.getNorthEastLon();
    }
}