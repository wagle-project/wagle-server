package com.waglewagle.server.global.security.config;

import com.waglewagle.server.global.security.handler.CustomAuthenticationEntryPoint;
import com.waglewagle.server.global.security.jwt.JwtFilter;
import com.waglewagle.server.global.security.jwt.JwtUtil;
import com.waglewagle.server.global.security.userdetails.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // 2. 아까 만든 에러 처리기 주입받기
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                // 1. 예외 처리 설정 등록
                .exceptionHandling(exception -> exception
                                .authenticationEntryPoint(customAuthenticationEntryPoint) // 401 에러(로그인 필요) 담당
                )

                // 2. 경로별 권한 설정
                .authorizeHttpRequests(authorize -> authorize
                        // 스웨거 및 health의 경우 인증 불필요
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs", "/health").permitAll()

                        // 나머지는 다 인증 필요
                        .anyRequest().authenticated()
                )

                // 3. JwtFilter를 시큐리티 필터 체인에 끼워 넣기
                .addFilterBefore(new JwtFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}