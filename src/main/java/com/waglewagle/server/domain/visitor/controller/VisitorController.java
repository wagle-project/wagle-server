package com.waglewagle.server.domain.visitor.controller;

import com.waglewagle.server.domain.visitor.dto.CongestionDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorDTO;
import com.waglewagle.server.domain.visitor.dto.VisitorLocationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VisitorController implements VisitorControllerDocs {
    @PostMapping("/api/v1/visitors")
    @Override
    public ResponseEntity<VisitorDTO.VisitorResponse> registerVisitor(VisitorDTO.VisitorRequest request) {
        return ResponseEntity.status(201).body(null);
    }

    @GetMapping("/api/v1/visitors/me")
    @Override
    public VisitorDTO.VisitorMeResponse getMyStatus() {
        return null;
    }

    @PostMapping("/api/v1/festivals/{festivalId}/location")
    @Override
    public VisitorLocationDTO.LocationUpdateResponse updateLocation(Long festivalId, VisitorLocationDTO.LocationUpdateRequest request) {
        return null;
    }

    @GetMapping("/api/v1/maps/{mapId}/congestion")
    @Override
    public CongestionDTO.CongestionResponse getCongestion(Long mapId) {
        return null;
    }
}
