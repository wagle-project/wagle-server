package com.waglewagle.server.domain.visitor.controller;

import com.waglewagle.server.domain.visitor.dto.CongestionDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorLocationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface VisitorControllerDocs {

    @Operation(summary = "방문자 등록 및 토큰 발급", description = "최초 접속 시 UUID를 생성하고 JWT를 발급합니다.")
    @ApiResponse(responseCode = "201", description = "생성 성공")
    @PostMapping("/api/v1/visitors")
    ResponseEntity<VisitorDTO.VisitorResponse> registerVisitor(@RequestBody VisitorDTO.VisitorRequest request);

    @Operation(summary = "내 접속 상태 확인", description = "토큰 유효성 및 약관 동의 상태를 확인합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/api/v1/visitors/me")
    ResponseEntity<VisitorDTO.VisitorMeResponse> getMyStatus();

    @Operation(summary = "내 위치 전송 및 갱신", description = "GPS 좌표를 서버로 갱신합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/api/v1/festivals/{festivalId}/location")
    ResponseEntity<VisitorLocationDTO.LocationUpdateResponse> updateLocation(
            @PathVariable Long festivalId,
            @RequestBody VisitorLocationDTO.LocationUpdateRequest request);

    @Operation(summary = "실시간 혼잡도 조회", description = "H3 구역별 혼잡도를 조회합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/api/v1/maps/{mapId}/congestion")
    ResponseEntity<CongestionDTO.CongestionResponse> getCongestion(@PathVariable Long mapId);
}
