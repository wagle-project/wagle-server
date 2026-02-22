package com.waglewagle.server.domain.visitor.controller;

import com.waglewagle.server.domain.visitor.dto.CongestionDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorLocationDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface VisitorControllerDocs {
    @Operation(summary = "동의 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "비즈니스 로직 에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "필수 값 누락",
                                    value = "{\"isSuccess\":false, \"code\":\"COMMON400\", " +
                                            "\"message\":\"잘못된 요청입니다.\", " +
                                            "\"result\":null}")
                    )
            )
    })
    @PostMapping("/api/v1/visitors")
    ApiResponse<VisitorDTO.VisitorResponse> registerVisitor(
            @RequestBody VisitorDTO.VisitorRequest request);

    @Operation(summary = "내 접속 상태 확인 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "인증 에러",
                                    value = "{\"isSuccess\":false, \"code\":\"AUTH4000\", " +
                                            "\"message\":\"로그인이 필요합니다.\", " +
                                            "\"result\":null}")
                    )
            )
    })
    @GetMapping("/api/v1/visitors/me")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<VisitorDTO.VisitorMeResponse> getMyStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "내 위치 조회 및 갱신 API")
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
            )
    })
    @PostMapping("/api/v1/festivals/{festivalId}/location")
    ApiResponse<VisitorLocationDTO.LocationUpdateResponse> updateLocation(
            @PathVariable Long festivalId,
            @RequestBody VisitorLocationDTO.LocationUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails);

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
    @GetMapping("/api/v1/maps/{mapId}/congestion")
    ApiResponse<CongestionDTO.CongestionResponse> getCongestion(
            @PathVariable Long mapId,
            @AuthenticationPrincipal CustomUserDetails userDetails);
}