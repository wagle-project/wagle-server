package com.waglewagle.server.domain.visitor.dto;

import com.waglewagle.server.domain.visitor.entity.Visitor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class VisitorDTO {

    @Schema(description = "방문자 등록 요청 (최초 로그인)")
    public record VisitorRequest(
            @Schema(description = "축제 ID", example = "1")
            Long festivalId,

            @Schema(description = "약관 동의 여부", example = "true")
            Boolean isTermsAgreed
    ) {}

    @Schema(description = "방문자 등록 완료 응답")
    public record VisitorResponse(
            @Schema(description = "서버 생성 UUID (ID)", example = "550e8400-e29b-41d4-a716-446655440000")
            String uuid,

            @Schema(description = "JWT 액세스 토큰", example = "eyJhbGciOiJIUzI1...")
            String accessToken,

            @Schema(description = "최초 등록 시간", example = "2026-02-18T12:28:14.15784")
            LocalDateTime createdAt
    ) {
        /**
         * Entity -> DTO 변환 Mapper
         * 토큰은 별도로 생성하여 주입받는 구조로 설계했습니다.
         */
        public static VisitorResponse of(Visitor visitor, String accessToken) {
            return new VisitorResponse(
                    visitor.getUuid(),
                    accessToken,
                    visitor.getCreatedAt()
            );
        }
    }

    @Schema(description = "내 접속 상태 확인 응답")
    public record VisitorMeResponse(
            @Schema(description = "사용자 고유 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
            String uuid,

            @Schema(description = "약관 동의 여부 (false일 경우 약관 팝업 필요)", example = "true")
            boolean isTermsAgreed,

            @Schema(description = "현재 참여 중인 축제 ID (없으면 null)", example = "1")
            Long festivalId
    ) {
        public static VisitorMeResponse from(Visitor visitor) {
            return new VisitorMeResponse(
                    visitor.getUuid(),
                    visitor.getIsTermsAgreed(),
                    visitor.getFestival() != null ? visitor.getFestival().getId() : null
            );
        }
    }
}
