package com.waglewagle.server.domain.visitor.controller;

import com.waglewagle.server.domain.visitor.dto.CongestionDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorLocationDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.apiPayload.code.GeneralSuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VisitorController implements VisitorControllerDocs {
    @PostMapping("/api/v1/visitors")
    @Override
    public ResponseEntity<ApiResponse<VisitorDTO.VisitorResponse>> registerVisitor(
            @RequestBody VisitorDTO.VisitorRequest request) {
        return ResponseEntity.ok(ApiResponse.onSuccess(GeneralSuccessCode.OK, null));
    }

    @GetMapping("/api/v1/visitors/me")
    @Override
    public ResponseEntity<ApiResponse<VisitorDTO.VisitorMeResponse>> getMyStatus() {
        return ResponseEntity.ok(ApiResponse.onSuccess(GeneralSuccessCode.OK, null));
    }

    @PostMapping("/api/v1/festivals/{festivalId}/location")
    @Override
    public ResponseEntity<ApiResponse<VisitorLocationDTO.LocationUpdateResponse>> updateLocation(
            @PathVariable Long festivalId,
            VisitorLocationDTO.LocationUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.onSuccess(GeneralSuccessCode.OK, null));
    }

    @GetMapping("/api/v1/maps/{mapId}/congestion")
    @Override
    public ResponseEntity<ApiResponse<CongestionDTO.CongestionResponse>> getCongestion(
            @PathVariable Long mapId) {
        return ResponseEntity.ok(ApiResponse.onSuccess(GeneralSuccessCode.OK, null));
    }
}
