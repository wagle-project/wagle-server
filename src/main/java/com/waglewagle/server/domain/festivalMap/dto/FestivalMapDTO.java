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

    @Schema(description = "지도별 실시간 혼잡도 응답")
    public record CongestionResponse(
            @Schema(description = "데이터 생성 타임스탬프 (Epoch MS)", example = "1707137143000")
            long timestamp,

            @Schema(description = "해당 지도 내 총 방문자 수", example = "540")
            int totalCount,

            @Schema(description = "H3 육각형 구역별 혼잡도 목록")
            List<ZoneInfo> zones
    ) {}

    @Schema(description = "H3 육각형 구역 정보")
    public record ZoneInfo(
            @Schema(description = "H3 인덱스 (Resolution에 따른 64비트 주소)", example = "8928308280fffff")
            String h3Index,

            @Schema(description = "구역 내 인원 수 (디버깅/상세용)", example = "15")
            int count,

            @Schema(description = "혼잡도 레벨 (0:쾌적, 1:보통, 2:혼잡, 3:매우혼잡)", example = "2")
            int level
    ) {}
}
