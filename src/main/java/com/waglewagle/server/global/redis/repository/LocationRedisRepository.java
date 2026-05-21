package com.waglewagle.server.global.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
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
    // 학생회 요청으로 만료 시간을 기존 30초에서 2분(120초)으로 연장
    private static final Duration VISITOR_TTL = Duration.ofMinutes(2);
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
        // "visitor:*:location" 패턴의 모든 키를 가져옴 (KEYS 대신 안전한 SCAN 방식으로 변경)
        Set<String> keys = new HashSet<>();
        ScanOptions options = ScanOptions.scanOptions()
                .match(VISITOR_KEY_PREFIX + "*:location")
                .count(1000) // 한 번에 1000개씩 쪼개서 조회 (레디스 블로킹 방지)
                .build();

        redisTemplate.execute((RedisCallback<Void>) connection -> {
            try (Cursor<byte[]> cursor = connection.scan(options)) {
                while (cursor.hasNext()) {
                    keys.add(new String(cursor.next(), StandardCharsets.UTF_8));
                }
            } catch (Exception e) {
                throw new RuntimeException("Redis SCAN 실행 중 예외 발생", e);
            }
            return null;
        });

        if (keys.isEmpty()) return Map.of();

        // 맵 아이디 필터링 및 H3 인덱스별 카운팅
        return keys.stream()
                .map(key -> redisTemplate.opsForValue().get(key))
                .filter(value -> value != null && value.startsWith(mapId + ":"))
                .map(value -> value.split(":")[1]) // "mapId:h3Index"에서 h3Index 추출
                .collect(Collectors.groupingBy(h3 -> h3, Collectors.summingInt(e -> 1)));
    }
}