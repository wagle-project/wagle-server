package com.waglewagle.server.domain.visitor.controller;

import com.waglewagle.server.domain.visitor.dto.CongestionDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/maps")
public interface CongestionControllerDocs {
    @Operation(summary = "실시간 혼잡도 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "로그인 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "인증 에러",
                                    value = "{\"isSuccess\":false, \"code\":\"AUTH4000\", " +
                                            "\"message\":\"로그인이 필요합니다.\", " +
                                            "\"result\":null}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "잘못된 경로",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "지도 없음",
                                    value = "{\"isSuccess\":false, \"code\":\"COMMON404\", " +
                                            "\"message\":\"해당 지도가 존재하지 않습니다.\", " +
                                            "\"result\":null}")
                    )
            )
    })
    @GetMapping("/{mapId}/congestion")
    ApiResponse<CongestionDTO.CongestionResponse> getCongestion(
            @PathVariable Long mapId,
            @AuthenticationPrincipal CustomUserDetails userDetails);
}
