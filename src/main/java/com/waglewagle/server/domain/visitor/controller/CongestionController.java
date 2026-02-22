package com.waglewagle.server.domain.visitor.controller;

import com.waglewagle.server.domain.visitor.dto.CongestionDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.apiPayload.code.GeneralSuccessCode;
import com.waglewagle.server.global.security.userdetails.CustomUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/maps")
public class CongestionController implements CongestionControllerDocs {
    @GetMapping("/{mapId}/congestion")
    @PreAuthorize("isAuthenticated()")
    @Override
    public ApiResponse<CongestionDTO.CongestionResponse> getCongestion(
            @PathVariable Long mapId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
    }
}
