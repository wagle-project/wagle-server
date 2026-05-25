package com.waglewagle.server.global.redis.interceptor;

import com.waglewagle.server.global.redis.annotation.TrackDau;
import com.waglewagle.server.global.redis.service.DauService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class DauInterceptor implements HandlerInterceptor {

    private final DauService dauService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            TrackDau trackDau = handlerMethod.getMethodAnnotation(TrackDau.class);

            if (trackDau != null) {
                String identifier = extractIdentifier(request, trackDau.type());
                log.info("Tracking DAU - URI: {}, Method: {}, Identifier: ({}) {}", 
                        request.getRequestURI(), request.getMethod(), trackDau.type(), identifier);
                dauService.addVisitor(identifier);
            }
        }
        return true;
    }

    private String extractIdentifier(HttpServletRequest request, TrackDau.IdentifierType type) {
        if (type == TrackDau.IdentifierType.SESSION_ID) {
            return request.getSession(true).getId();
        }

        // IP 추출 로직 (프록시 환경 완벽 대처)
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // X-Forwarded-For 헤더에 쉼표(,)로 구분된 다중 IP가 유입될 경우 첫 번째 IP(실제 클라이언트 IP)만 추출
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
