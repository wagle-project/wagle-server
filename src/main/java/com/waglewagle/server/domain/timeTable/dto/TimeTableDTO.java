package com.waglewagle.server.domain.timeTable.dto;

import com.waglewagle.server.domain.timeTable.entity.TimeTable;
import io.swagger.v3.oas.annotations.media.Schema;

public class TimeTableDTO {
    @Schema(description = "타임테이블 상세 정보")
    public record TimeTableInfo(
            @Schema(description = "타임테이블 고유 ID", example = "1")
            Long id,

            @Schema(description = "타임테이블 이미지 URL", example = "https://s3.../timetable.jpg")
            String imageUrl,

            @Schema(description = "노출 순서 (오름차순 정렬용)", example = "1")
            int sequence
    ) {
        /**
         * Entity -> Record 변환 Mapper
         */
        public static TimeTableInfo from(TimeTable entity) {
            return new TimeTableInfo(
                    entity.getId(),
                    entity.getImageUrl(),
                    entity.getSequence()
            );
        }
    }
}
