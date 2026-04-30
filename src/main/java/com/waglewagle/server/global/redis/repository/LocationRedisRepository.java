package com.waglewagle.server.global.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Redis 키 구조
 * 방문자 위치: visitor:{uuid}:location → {mapId}:{h3Index} (TTL 30초)
 * 셀 카운트: map:{mapId}:cell:{h3Index} → 인원 수 (String, incr/decr)
 * **/

@Repository
@RequiredArgsConstructor
public class LocationRedisRepository {

    private final StringRedisTemplate redisTemplate;

    private static final Duration VISITOR_TTL = Duration.ofSeconds(30);
    private static final String VISITOR_KEY = "visitor:%s:location";
    private static final String CELL_KEY = "map:%d:cell:%s";
    private static final String CELL_PATTERN = "map:%d:cell:*";

    // 방문자 이전 위치 조회
    public Optional<String> getVisitorLocation(String uuid) {
        String value = redisTemplate.opsForValue().get(VISITOR_KEY.formatted(uuid));
        return Optional.ofNullable(value);
    }

    // 방문자 위치 저장 (TTL 갱신)
    public void saveVisitorLocation(String uuid, Long mapId, String h3Index) {
        String key = VISITOR_KEY.formatted(uuid);
        String value = mapId + ":" + h3Index;
        redisTemplate.opsForValue().set(key, value, VISITOR_TTL);
    }

    // 방문자 위치 키 삭제
    public void deleteVisitorLocation(String uuid) {
        redisTemplate.delete(VISITOR_KEY.formatted(uuid));
    }

    // 셀 카운트 증가
    public void incrementCell(Long mapId, String h3Index) {
        String key = CELL_KEY.formatted(mapId, h3Index);
        redisTemplate.opsForValue().increment(key);
    }

    // 셀 카운트 감소 (0 이하면 키 삭제)
    public void decrementCell(Long mapId, String h3Index) {
        String key = CELL_KEY.formatted(mapId, h3Index);
        Long count = redisTemplate.opsForValue().decrement(key);
        if (count != null && count <= 0) {
            redisTemplate.delete(key);
        }
    }

    // 맵의 전체 셀 목록 조회
    public Map<String, Integer> getCellCounts(Long mapId) {
        String pattern = CELL_PATTERN.formatted(mapId);
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys == null || keys.isEmpty()) return Map.of();

        Map<String, Integer> result = new HashMap<>();
        for (String key : keys) {
            String value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                // 키에서 h3Index 추출: "map:{mapId}:cell:{h3Index}"
                String h3Index = key.substring(key.lastIndexOf(":") + 1);
                result.put(h3Index, Integer.parseInt(value));
            }
        }
        return result;
    }
}