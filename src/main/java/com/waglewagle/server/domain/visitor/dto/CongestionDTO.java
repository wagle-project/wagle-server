package com.waglewagle.server.domain.visitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class CongestionDTO {
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
