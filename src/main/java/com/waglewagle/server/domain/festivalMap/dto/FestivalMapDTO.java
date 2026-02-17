package com.waglewagle.server.domain.festivalMap.dto;

import com.waglewagle.server.domain.festivalMap.entity.FestivalMap;
import io.swagger.v3.oas.annotations.media.Schema;

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
}
