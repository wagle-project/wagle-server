package com.waglewagle.server.global.redis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @BeforeEach
    void setUp() {
        todayKey = "dau:" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Test
    void addVisitor_success() {
        // given
        String identifier = "192.168.0.1";
        when(redisTemplate.opsForHyperLogLog()).thenReturn(hyperLogLogOperations);
        when(hyperLogLogOperations.add(eq(todayKey), eq(identifier))).thenReturn(1L);

        // when
        dauService.addVisitor(identifier);

        // then
        verify(hyperLogLogOperations, times(1)).add(eq(todayKey), eq(identifier));
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
}
