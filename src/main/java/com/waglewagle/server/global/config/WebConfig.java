package com.waglewagle.server.global.config;

import com.waglewagle.server.global.redis.interceptor.DauInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final DauInterceptor dauInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 모든 경로를 대상으로 인터셉터를 추가하되, 실제 적용 여부는 인터셉터 내부에서 @TrackDau 존재 여부로 판단합니다.
        registry.addInterceptor(dauInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**", "/health");
    }
}
