package com.waglewagle.server.domain.visitor.controller;

import com.waglewagle.server.domain.visitor.dto.CongestionDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorLocationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface VisitorControllerDocs {
    @Operation(summary = "동의 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "500", description = "실패")
    })
    @PostMapping("/api/v1/visitors")
    ResponseEntity<VisitorDTO.VisitorResponse> registerVisitor(@RequestBody VisitorDTO.VisitorRequest request);

    @Operation(summary = "내 접속 상태 확인 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "실패")
    })
    @GetMapping("/api/v1/visitors/me")
    VisitorDTO.VisitorMeResponse getMyStatus();

    @Operation(summary = "내 위치 조회 및 갱신 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "실패")
    })
    @PostMapping("/api/v1/festivals/{festivalId}/location")
    VisitorLocationDTO.LocationUpdateResponse updateLocation(
            @PathVariable Long festivalId,
            @RequestBody VisitorLocationDTO.LocationUpdateRequest request);

    @Operation(summary = "실시간 혼잡도 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "실패")
    })
    @GetMapping("/api/v1/maps/{mapId}/congestion")
    CongestionDTO.CongestionResponse getCongestion(@PathVariable Long mapId);
}
