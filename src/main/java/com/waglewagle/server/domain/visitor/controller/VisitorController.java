package com.waglewagle.server.domain.visitor.controller;

import com.waglewagle.server.domain.visitor.dto.VisitorDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.apiPayload.code.GeneralSuccessCode;
import com.waglewagle.server.global.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/visitors")
public class VisitorController implements VisitorControllerDocs {
    @PostMapping("")
    @Override
    public ApiResponse<VisitorDTO.VisitorResponse> registerVisitor(
            @RequestBody VisitorDTO.VisitorRequest request) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Override
    public ApiResponse<VisitorDTO.VisitorMeResponse> getMyStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
    }
}
