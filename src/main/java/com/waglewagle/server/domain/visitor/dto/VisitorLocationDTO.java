package com.waglewagle.server.domain.visitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class VisitorLocationDTO {
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
