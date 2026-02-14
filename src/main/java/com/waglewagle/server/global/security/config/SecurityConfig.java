package com.waglewagle.server.global.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 불필요한 기본 설정 비활성화 (CSRF, Basic, Form)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                // 2. 인증/인가 설정
                .authorizeHttpRequests(authorize -> authorize
                        // [허용] Swagger 관련 경로 (UI + API Docs 데이터)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // [허용] 헬스 체크
                        .requestMatchers("/health").permitAll()

                        // [차단] 그 외의 모든 요청은 인증(로그인) 필요
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}