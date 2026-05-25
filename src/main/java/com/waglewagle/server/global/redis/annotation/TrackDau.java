package com.waglewagle.server.global.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 특정 API 호출 시 일간 활성 사용자(DAU)를 집계하기 위한 어노테이션입니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackDau {

    enum IdentifierType {
        IP,
        SESSION_ID
    }

    /**
     * DAU 카운팅을 위한 클라이언트 식별 방법 (기본값: IP)
     */
    IdentifierType type() default IdentifierType.IP;
}
