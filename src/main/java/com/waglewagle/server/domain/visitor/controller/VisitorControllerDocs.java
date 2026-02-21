package com.waglewagle.server.domain.visitor.controller;

import com.waglewagle.server.domain.visitor.dto.CongestionDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorLocationDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface VisitorControllerDocs {
    @Operation(summary = "동의 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "비즈니스 로직 에러", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/api/v1/visitors")
    ResponseEntity<ApiResponse<VisitorDTO.VisitorResponse>> registerVisitor(
            @RequestBody VisitorDTO.VisitorRequest request);

    @Operation(summary = "내 접속 상태 확인 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 에러", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/api/v1/visitors/me")
    ResponseEntity<ApiResponse<VisitorDTO.VisitorMeResponse>> getMyStatus();

    @Operation(summary = "내 위치 조회 및 갱신 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/api/v1/festivals/{festivalId}/location")
    ResponseEntity<ApiResponse<VisitorLocationDTO.LocationUpdateResponse>> updateLocation(
            @PathVariable Long festivalId,
            @RequestBody VisitorLocationDTO.LocationUpdateRequest request);

    @Operation(summary = "실시간 혼잡도 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "잘못된 경로", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/api/v1/maps/{mapId}/congestion")
    ResponseEntity<ApiResponse<CongestionDTO.CongestionResponse>> getCongestion(
            @PathVariable Long mapId);
}