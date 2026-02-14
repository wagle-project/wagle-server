package com.waglewagle.server.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";

        // 1. 헤더에 토큰을 포함시키기 위한 요구사항 설정
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);

        // 2. 보안 스키마 설정 (Bearer 토큰 방식)
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(securityRequirement) // 모든 API에 좌물쇠 걸기
                .components(components);
    }

    private Info apiInfo() {
        return new Info()
                .title("API Wiki Project API")
                .description("API Wiki 프로젝트 명세서")
                .version("1.0.0");
    }
}