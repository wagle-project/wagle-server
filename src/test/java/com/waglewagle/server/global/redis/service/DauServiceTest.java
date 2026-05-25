package com.waglewagle.server.global.redis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DauServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private HyperLogLogOperations<String, String> hyperLogLogOperations;

    @InjectMocks
    private DauService dauService;

    private String todayKey;
    private String hourlyKey;

    @BeforeEach
    void setUp() {
        todayKey = "dau:" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        hourlyKey = "dau:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd:HH"));
    }

    @Test
    void addVisitor_success() {
        // given
        String identifier = "192.168.0.1";
        when(redisTemplate.opsForHyperLogLog()).thenReturn(hyperLogLogOperations);
        when(hyperLogLogOperations.add(eq(todayKey), eq(identifier))).thenReturn(1L);
        when(hyperLogLogOperations.add(eq(hourlyKey), eq(identifier))).thenReturn(1L);
        when(redisTemplate.expire(eq(hourlyKey), any(Duration.class))).thenReturn(true);

        // when
        dauService.addVisitor(identifier);

        // then
        verify(hyperLogLogOperations, times(1)).add(eq(todayKey), eq(identifier));
        verify(hyperLogLogOperations, times(1)).add(eq(hourlyKey), eq(identifier));
        verify(redisTemplate, times(1)).expire(eq(hourlyKey), eq(Duration.ofHours(48)));
    }

    @Test
    void getDauCount_success() {
        // given
        when(redisTemplate.opsForHyperLogLog()).thenReturn(hyperLogLogOperations);
        when(hyperLogLogOperations.size(eq(todayKey))).thenReturn(5L);

        // when
        long count = dauService.getDauCount();

        // then
        assertEquals(5L, count);
        verify(hyperLogLogOperations, times(1)).size(eq(todayKey));
    }

    @Test
    void getHourlyDauCount_success() {
        // given
        when(redisTemplate.opsForHyperLogLog()).thenReturn(hyperLogLogOperations);
        when(hyperLogLogOperations.size(eq(hourlyKey))).thenReturn(3L);

        // when
        long count = dauService.getHourlyDauCount();

        // then
        assertEquals(3L, count);
        verify(hyperLogLogOperations, times(1)).size(eq(hourlyKey));
    }
}
