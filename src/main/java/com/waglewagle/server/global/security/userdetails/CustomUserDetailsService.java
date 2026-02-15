package com.waglewagle.server.global.security.userdetails;

import com.waglewagle.server.domain.visitor.entity.Visitor;
import com.waglewagle.server.domain.visitor.repository.VisitorRepository;
import com.waglewagle.server.global.apiPayload.code.GeneralErrorCode;
import com.waglewagle.server.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final VisitorRepository visitorRepository;

    @Override
    public UserDetails loadUserByUsername(String uuid) {
        // 식별자 uuid로 해당하는 유저(Visitor) 찾기
        Visitor visitor = visitorRepository.findById(uuid)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.USER_NOT_FOUND));

        return new CustomUserDetails(visitor);
    }
}