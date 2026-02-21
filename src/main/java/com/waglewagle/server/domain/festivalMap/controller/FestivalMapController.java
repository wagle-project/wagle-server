package com.waglewagle.server.domain.festivalMap.controller;

import com.waglewagle.server.domain.festivalMap.dto.FestivalMapDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.apiPayload.code.GeneralSuccessCode;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FestivalMapController implements FestivalMapControllerDocs {
    @GetMapping("/api/v1/festivals/{festivalId}/maps")
    @Override
    public ResponseEntity<ApiResponse<ListResponseDTO<FestivalMapDTO.FestivalMapInfo>>> getFestivalMaps(
            @PathVariable Long festivalId) {
        return ResponseEntity.ok(ApiResponse.onSuccess(GeneralSuccessCode.OK, null));
    }
}
