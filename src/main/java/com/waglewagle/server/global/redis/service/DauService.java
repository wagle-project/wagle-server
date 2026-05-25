package com.waglewagle.server.global.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class DauService {

    private final StringRedisTemplate redisTemplate;

    private static final String DAU_KEY_PREFIX = "dau:";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 오늘 날짜의 Redis 키에 클라이언트 식별자를 추가합니다. (PFADD)
     *
     * @param identifier 클라이언트 식별자 (IP or Session ID 등)
     */
    public void addVisitor(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return;
        }
        String todayKey = getTodayKey();
        try {
            redisTemplate.opsForHyperLogLog().add(todayKey, identifier);
            log.debug("Successfully added identifier: {} to HyperLogLog key: {}", identifier, todayKey);
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
     * 오늘 날짜의 Redis Key를 생성합니다. (예: dau:2026-05-25)
     */
    private String getTodayKey() {
        return DAU_KEY_PREFIX + LocalDate.now().format(DATE_FORMATTER);
    }
}
