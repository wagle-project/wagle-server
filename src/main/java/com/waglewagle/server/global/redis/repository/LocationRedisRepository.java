package com.waglewagle.server.global.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    private static final String VISITOR_KEY_PREFIX = "visitor:";
    private static final String VISITOR_KEY = "visitor:%s:location";

    // 1. 방문자 위치 저장
    public void saveVisitorLocation(String uuid, Long mapId, String h3Index) {
        String key = VISITOR_KEY.formatted(uuid);
        String value = mapId + ":" + h3Index;
        redisTemplate.opsForValue().set(key, value, VISITOR_TTL);
    }

    // 2. 방문자 위치 삭제
    public void deleteVisitorLocation(String uuid) {
        redisTemplate.delete(VISITOR_KEY.formatted(uuid));
    }

    // 3. 실시간으로 살아있는 키들을 전수조사해서 카운트 계산
    public Map<String, Integer> getCellCounts(Long mapId) {
        // "visitor:*:location" 패턴의 모든 키를 가져옴
        Set<String> keys = redisTemplate.keys(VISITOR_KEY_PREFIX + "*:location");
        if (keys == null || keys.isEmpty()) return Map.of();

        // 맵 아이디 필터링 및 H3 인덱스별 카운팅
        return keys.stream()
                .map(key -> redisTemplate.opsForValue().get(key))
                .filter(value -> value != null && value.startsWith(mapId + ":"))
                .map(value -> value.split(":")[1]) // "mapId:h3Index"에서 h3Index 추출
                .collect(Collectors.groupingBy(h3 -> h3, Collectors.summingInt(e -> 1)));
    }
}