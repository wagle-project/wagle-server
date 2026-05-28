package com.waglewagle.server.global.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class DauService {

    private final StringRedisTemplate redisTemplate;
    private final ZoneId zoneId;

    private static final String DAU_KEY_PREFIX = "dau:";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter HOURLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH");

    public DauService(StringRedisTemplate redisTemplate,
                      @Value("${app.timezone:Asia/Seoul}") String timezone) {
        this.redisTemplate = redisTemplate;
        this.zoneId = ZoneId.of(timezone);
        log.info("Initialized DauService with timezone: {}", this.zoneId);
    }

    /**
     * 오늘 날짜의 Redis 키 및 현재 시간대별 키에 클라이언트 식별자를 추가합니다. (PFADD)
     *
     * @param identifier 클라이언트 식별자 (IP or Session ID 등)
     */
    public void addVisitor(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return;
        }
        String todayKey = getTodayKey();
        String hourlyKey = getHourlyKey();
        try {
            // 1. 일간 활성 사용자 수 집계 (DAU)
            redisTemplate.opsForHyperLogLog().add(todayKey, identifier);

            // 2. 시간대별 활성 사용자 수 집계 (HAU)
            redisTemplate.opsForHyperLogLog().add(hourlyKey, identifier);
            // 메모리 관리를 위해 시간대별 키에는 48시간의 TTL 부여
            redisTemplate.expire(hourlyKey, Duration.ofHours(48));

            log.debug("Successfully added identifier: {} to HyperLogLog keys: {}, {}", identifier, todayKey, hourlyKey);
        } catch (Exception e) {
            log.error("Failed to add visitor identifier to Redis HyperLogLog", e);
        }
    }

    /**
     * 오늘 날짜의 Redis 키에 저장된 고유 사용자 수(DAU)를 조회합니다. (PFCOUNT)
     *
     * @return 일간 활성 사용자 수
     */
    public long getDauCount() {
        String todayKey = getTodayKey();
        try {
            Long count = redisTemplate.opsForHyperLogLog().size(todayKey);
            return count != null ? count : 0L;
        } catch (Exception e) {
            log.error("Failed to count DAU from Redis HyperLogLog, returning 0", e);
            return 0L;
        }
    }

    /**
     * 현재 시간대별 Redis 키에 저장된 고유 사용자 수(HAU)를 조회합니다. (PFCOUNT)
     *
     * @return 시간대별 활성 사용자 수
     */
    public long getHourlyDauCount() {
        String hourlyKey = getHourlyKey();
        try {
            Long count = redisTemplate.opsForHyperLogLog().size(hourlyKey);
            return count != null ? count : 0L;
        } catch (Exception e) {
            log.error("Failed to count Hourly Active Users from Redis HyperLogLog, returning 0", e);
            return 0L;
        }
    }

    /**
     * 오늘 날짜의 Redis Key를 생성합니다. (예: dau:2026-05-25)
     */
    private String getTodayKey() {
        return DAU_KEY_PREFIX + LocalDate.now(zoneId).format(DATE_FORMATTER);
    }

    /**
     * 현재 시간대별 Redis Key를 생성합니다. (예: dau:2026-05-25:21)
     */
    private String getHourlyKey() {
        return DAU_KEY_PREFIX + LocalDateTime.now(zoneId).format(HOURLY_FORMATTER);
    }
}
