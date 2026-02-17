package com.waglewagle.server.domain.festival.dto;

import com.waglewagle.server.domain.festival.entity.Festival;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public class FestivalDTO {

    @Schema(description = "축제 요약 정보 (추천/목록 응답용)")
    public record FestivalSummary(
            @Schema(description = "축제 고유 ID", example = "1")
            Long id,

            @Schema(description = "축제 명칭", example = "2026 김천 김밥 축제")
            String name,

            @Schema(description = "S3 포스터 이미지 URL")
            String posterUrl,

            @Schema(description = "축제 진행 상태 (UPCOMING, ONGOING, ENDED)", example = "ONGOING")
            String status,

            @Schema(description = "축제 대표 장소/랜드마크", example = "김천 사명대사공원")
            String placeName
    ) {
        /**
         * Entity -> DTO 변환 Mapper
         * 현재 시간을 기준으로 축제의 진행 상태를 계산하여 매핑합니다. [cite: 10]
         */
        public static FestivalSummary from(Festival festival) {
            return new FestivalSummary(
                    festival.getId(),
                    festival.getName(),
                    festival.getPosterUrl(),
                    calculateStatus(festival.getStartDate(), festival.getEndDate()),
                    festival.getPlaceName()
            );
        }

        /**
         * 축제 기간을 바탕으로 상태를 판별하는 도메인 로직입니다.
         */
        private static String calculateStatus(LocalDateTime start, LocalDateTime end) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(start)) return "UPCOMING";
            if (now.isAfter(end)) return "ENDED";
            return "ONGOING";
        }
    }
}