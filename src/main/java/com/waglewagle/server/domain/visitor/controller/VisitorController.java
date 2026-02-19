package com.waglewagle.server.domain.visitor.controller;

import com.waglewagle.server.domain.visitor.dto.CongestionDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorLocationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VisitorController implements VisitorDocs {
    private final VisitorService visitorService;
    private final CongestionService congestionService;

    @Override
    public ResponseEntity<VisitorDTO.VisitorResponse> registerVisitor(VisitorDTO.VisitorRequest request) {
        return ResponseEntity.status(201).body(visitorService.createVisitor(request));
    }

    @Override
    public ResponseEntity<VisitorDTO.VisitorMeResponse> getMyStatus() {
        return ResponseEntity.ok(visitorService.getCurrentStatus());
    }

    @Override
    public ResponseEntity<VisitorLocationDTO.LocationUpdateResponse> updateLocation(Long festivalId, VisitorLocationDTO.LocationUpdateRequest request) {
        return ResponseEntity.ok(visitorService.updateLocation(festivalId, request));
    }

    @Override
    public ResponseEntity<CongestionDTO.CongestionResponse> getCongestion(Long mapId) {
        return ResponseEntity.ok(congestionService.getCongestion(mapId));
    }
}
