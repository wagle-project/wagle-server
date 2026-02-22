package com.waglewagle.server.domain.festivalMap.controller;

import com.waglewagle.server.domain.festivalMap.dto.FestivalMapDTO;
import com.waglewagle.server.domain.festivalMap.dto.CongestionDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.apiPayload.code.GeneralSuccessCode;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import com.waglewagle.server.global.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FestivalMapController implements FestivalMapControllerDocs {
    @GetMapping("/festivals/{festivalId}/maps")
    @PreAuthorize("isAuthenticated()")
    @Override
    public ApiResponse<ListResponseDTO<FestivalMapDTO.FestivalMapInfo>> getFestivalMaps(
            @PathVariable Long festivalId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onListSuccess(GeneralSuccessCode.OK, null);
    }

    @GetMapping("/maps/{mapId}/congestion")
    @PreAuthorize("isAuthenticated()")
    @Override
    public ApiResponse<CongestionDTO.CongestionResponse> getCongestion(
            @PathVariable Long mapId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
    }
}
