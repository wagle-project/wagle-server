package com.waglewagle.server.domain.festivalMap.controller;

import com.waglewagle.server.domain.festivalMap.dto.FestivalMapDTO;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FestivalMapController implements FestivalMapDocs {

    private final FestivalMapService festivalMapService;

    @Override
    public ResponseEntity<ListResponseDTO<FestivalMapDTO.FestivalMapInfo>> getFestivalMaps(Long festivalId) {
        return ResponseEntity.ok(ListResponseDTO.of(festivalMapService.getMapsByFestival(festivalId)));
    }
}
