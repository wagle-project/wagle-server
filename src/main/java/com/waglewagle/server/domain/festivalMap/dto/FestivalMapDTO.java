package com.waglewagle.server.domain.festivalMap.dto;

import com.waglewagle.server.domain.festivalMap.entity.FestivalMap;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class FestivalMapDTO {
    @Schema(description = "축제 지도 상세 정보")
    public record FestivalMapInfo(
            @Schema(description = "노출 순서", example = "1")
            int sequence,

            @Schema(description = "지도 이미지 ID", example = "7")
            Long mapId,

            @Schema(description = "지도 이미지 URL")
            String mapImageUrl,

            @Schema(description = "지도 좌표 경계 (Ground Overlay용)")
            MapBounds bounds
    ) {
        public static FestivalMapInfo from(FestivalMap entity) {
            return new FestivalMapInfo(
                    entity.getSequence(),
                    entity.getId(),
                    entity.getImageUrl(),
                    new MapBounds(
                            new LatLng(entity.getSouthWestLat(), entity.getSouthWestLon()),
                            new LatLng(entity.getNorthEastLat(), entity.getNorthEastLon())
                    )
            );
        }
    }

    @Schema(description = "지도 좌표 경계 정보")
    public record MapBounds(
            @Schema(description = "남서쪽 좌표 (South-West)")
            LatLng southWest,

            @Schema(description = "북동쪽 좌표 (North-East)")
            LatLng northEast
    ) {}

    @Schema(description = "위경도 좌표")
    public record LatLng(
            @Schema(description = "위도", example = "37.566000")
            Double lat,

            @Schema(description = "경도", example = "126.977000")
            Double lng
    ) {}

    @Schema(description = "방문자 위치 갱신 요청")
    public record LocationUpdateRequest(
            @Schema(description = "현재 위도", example = "35.8322")
            Double lat,

            @Schema(description = "현재 경도", example = "128.7576")
            Double lng
    ) {}

    @Schema(description = "방문자 위치 갱신 응답")
    public record LocationUpdateResponse(
            @Schema(description = "축제 구역(Map) 내부 여부", example = "true")
            boolean isInside,

            @Schema(description = "현재 위치한 지도 ID (구역 밖이면 null)", example = "7")
            Long currentMapId,

            @Schema(description = "서버 권장 호출 간격 (ms)", example = "3000")
            long locationUpdateInterval
    ) {}
}
