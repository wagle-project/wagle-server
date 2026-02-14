package com.waglewagle.server.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;

public interface HealthControllerDocs {

    @Operation(
            summary = "서버 체크 API By 서현",
            description = "서버의 생존 여부를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "실패")
    })
    @GetMapping("/health")
    String healthCheck();
}
