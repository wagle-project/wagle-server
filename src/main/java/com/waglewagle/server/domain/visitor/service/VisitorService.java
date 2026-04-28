package com.waglewagle.server.domain.visitor.service;

import com.waglewagle.server.domain.visitor.dto.VisitorDTO;
import com.waglewagle.server.domain.visitor.entity.Visitor;
import com.waglewagle.server.domain.visitor.repository.VisitorRepository;
import com.waglewagle.server.global.apiPayload.exception.GeneralException;
import com.waglewagle.server.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.waglewagle.server.global.apiPayload.code.GeneralErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final JwtUtil jwtUtil;

    // 최초 로그인 (방문자 생성 및 토큰 발급)
    public VisitorDTO.VisitorResponse registerVisitor(VisitorDTO.VisitorRequest request) {
        // 1. 엔티티 생성 및 저장 (UUID, createdAt 자동 생성)
        Visitor visitor = Visitor.builder()
                .isTermsAgreed(request.isTermsAgreed())
                .build();

        Visitor savedVisitor = visitorRepository.save(visitor);

        // 2. JWT 토큰 발급
        String accessToken = jwtUtil.createAccessToken(savedVisitor.getUuid());

        return new VisitorDTO.VisitorResponse(
                savedVisitor.getUuid(),
                savedVisitor.getCreatedAt(),
                accessToken
        );
    }

    // 내 상태 확인용 방문자 조회
    public VisitorDTO.VisitorMeResponse getVisitorStatus(String uuid) {
        Visitor visitor = visitorRepository.findById(uuid)
                .orElseThrow(() -> new GeneralException(USER_NOT_FOUND));

        return new VisitorDTO.VisitorMeResponse(
                visitor.getUuid(),
                true
        );
    }
}