package com.waglewagle.server.domain.visitor.service;

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

        List<FestivalMap> maps = festivalMapRepository.findByFestivalId(festivalId);

        FestivalMap currentMap = maps.stream()
                .filter(m -> isInsideMap(m, lat, lng))
                .findFirst()
                .orElse(null);

        boolean isInside = currentMap != null;
        Long currentMapId = isInside ? currentMap.getId() : null;

        if (isInside) {
            // H3 인덱스 생성
            String h3Index = h3.latLngToCellAddress(lat, lng, H3_RESOLUTION);

            // 핵심: 그냥 저장만 함. 30초 TTL이 알아서 갱신됨.
            // 이전 위치를 찾아 뺄 필요가 없음! (다음 집계 때 자동으로 반영됨)
            locationRedisRepository.saveVisitorLocation(uuid, currentMapId, h3Index);
        } else {
            // 구역 밖이면 세션 삭제
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