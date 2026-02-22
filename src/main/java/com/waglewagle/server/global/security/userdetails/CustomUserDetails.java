package com.waglewagle.server.global.security.userdetails;

import com.waglewagle.server.domain.visitor.entity.Visitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Visitor visitor;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 방문자 권한 부여
        return List.of(new SimpleGrantedAuthority("ROLE_VISITOR"));
    }

    @Override
    public String getPassword() {
        return ""; // 투명 로그인이라 비밀번호 없음
    }

    @Override
    public String getUsername() {
        return visitor.getUuid(); // 식별자로 UUID 반환
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}