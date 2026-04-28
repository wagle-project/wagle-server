package com.waglewagle.server.domain.visitor.dto;

import com.waglewagle.server.domain.visitor.entity.Visitor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class VisitorDTO {

    @Schema(description = "방문자 등록 요청 (최초 로그인)")
    public record VisitorRequest(
            @Schema(description = "약관 동의 여부", example = "true")
            Boolean isTermsAgreed
    ) {}

    @Schema(description = "방문자 등록 완료 응답")
    public record VisitorResponse(
            @Schema(description = "서버 생성 UUID (ID)", example = "550e8400-e29b-41d4-a716-446655440000")
            String uuid,

            @Schema(description = "최초 등록 시간", example = "2026-02-18T12:28:14.15784")
            LocalDateTime createdAt,

            @Schema(description = "엑세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjMsImlhdCI6MTc0NzU2MTcwOCwiZXhwIjoxNzQ3NTY1MzA4fQ.Pwm5AKUkUC1gU4L0Am_qi_KGYQEHQ3JnANhr682NWY0")
            String accessToken
    ) {}

    @Schema(description = "내 접속 상태 확인 응답")
    public record VisitorMeResponse(
            @Schema(description = "사용자 고유 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
            String uuid,

            @Schema(description = "약관 동의 여부 (false일 경우 약관 팝업 필요)", example = "true")
            Boolean isTermsAgreed
    ) {
        public static VisitorMeResponse from(Visitor visitor) {
            return new VisitorMeResponse(
                    visitor.getUuid(),
                    visitor.getIsTermsAgreed()
            );
        }
    }
}
