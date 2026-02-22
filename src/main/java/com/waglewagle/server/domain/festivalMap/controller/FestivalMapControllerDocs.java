package com.waglewagle.server.domain.festivalMap.controller;

import com.waglewagle.server.domain.festivalMap.dto.FestivalMapDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import com.waglewagle.server.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface FestivalMapControllerDocs {
    @Operation(summary = "축제 지도 오버레이 조회 API")
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
                    responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "서버 에러",
                                    value = "{\"isSuccess\":false, \"code\":\"COMMON500\", " +
                                            "\"message\":\"서버 에러입니다.\", " +
                                            "\"result\":null}")
                    )
            )
    })
    @GetMapping("/api/v1/festivals/{festivalId}/maps")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<ListResponseDTO<FestivalMapDTO.FestivalMapInfo>> getFestivalMaps(
            @PathVariable Long festivalId,
            @AuthenticationPrincipal CustomUserDetails userDetails);
}
