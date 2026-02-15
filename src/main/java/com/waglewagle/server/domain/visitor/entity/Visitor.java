package com.waglewagle.server.domain.visitor.entity;

import com.waglewagle.server.domain.festival.entity.Festival;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "visitor")
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // UUID 자동 생성
    @Column(name = "uuid", columnDefinition = "VARCHAR(36)")
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @Column(name = "is_terms_agreed", nullable = false)
    private Boolean isTermsAgreed;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;

    // --- 비즈니스 로직 메서드 ---

    // API 호출 시 마지막 활동 시간 갱신
    public void updateLastActiveTime() {
        this.lastActiveAt = LocalDateTime.now();
    }

    // 축제 변경 (다른 축제로 이동 시)
    public void joinFestival(Festival festival) {
        this.festival = festival;
        this.updateLastActiveTime();
    }
}