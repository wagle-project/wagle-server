package com.waglewagle.server.global.redis.config;

import com.waglewagle.server.global.redis.service.DauService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DauMetricsConfig {

    private final DauService dauService;
    private final MeterRegistry meterRegistry;

    /**
     * Micrometer Gauge 메트릭들을 등록합니다.
     * /actuator/prometheus가 호출될 때마다 DauService의 비즈니스 메서드가 실시간 호출되어 Redis PFCOUNT 값을 반환합니다.
     */
    @PostConstruct
    public void registerDauMetrics() {
        // 일간 활성 사용자 수 (DAU)
        Gauge.builder("festival_dau_total", dauService, DauService::getDauCount)
                .description("Daily Active Users count using Redis HyperLogLog")
                .register(meterRegistry);

        // 시간대별 활성 사용자 수 (HAU)
        Gauge.builder("festival_hau_total", dauService, DauService::getHourlyDauCount)
                .description("Hourly Active Users count using Redis HyperLogLog")
                .register(meterRegistry);
    }
}
