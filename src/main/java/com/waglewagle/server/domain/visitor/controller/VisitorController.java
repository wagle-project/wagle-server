package com.waglewagle.server.domain.visitor.controller;

import com.waglewagle.server.domain.visitor.dto.VisitorDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorLocationDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.apiPayload.code.GeneralSuccessCode;
import com.waglewagle.server.global.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class VisitorController implements VisitorControllerDocs {
    @PostMapping("/visitors")
    @Override
    public ApiResponse<VisitorDTO.VisitorResponse> registerVisitor(
            @RequestBody VisitorDTO.VisitorRequest request) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
    }

    @GetMapping("/visitors/me")
    @PreAuthorize("isAuthenticated()")
    @Override
    public ApiResponse<VisitorDTO.VisitorMeResponse> getMyStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
    }

    @PostMapping("/festivals/{festivalId}/location")
    @PreAuthorize("isAuthenticated()")
    @Override
    public ApiResponse<VisitorLocationDTO.LocationUpdateResponse> updateLocation(
            @PathVariable Long festivalId,
            VisitorLocationDTO.LocationUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
    }
}
