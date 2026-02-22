package com.waglewagle.server.domain.visitor.controller;

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
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/visitors")
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
    @PostMapping("")
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
    @GetMapping("/me")
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
    @PostMapping("/festivals/{festivalId}/location")
    ApiResponse<VisitorLocationDTO.LocationUpdateResponse> updateLocation(
            @PathVariable Long festivalId,
            @RequestBody VisitorLocationDTO.LocationUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails);
}