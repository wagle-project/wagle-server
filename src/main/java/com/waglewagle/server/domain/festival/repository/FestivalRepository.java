package com.waglewagle.server.domain.festival.repository;

import com.waglewagle.server.domain.festival.entity.Festival;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FestivalRepository  extends JpaRepository<Festival, Long> {

    Page<Festival> findByNameContaining(String keyword, Pageable pageable);

    // 1순위: 개최 중 (시작일 <= 현재 <= 종료일) 중 시작일 최신순
    @Query("SELECT f FROM Festival f WHERE f.startAt <= :now AND f.endAt >= :now ORDER BY f.startAt DESC")
    List<Festival> findOngoing(@Param("now") LocalDateTime now, Pageable pageable);

    // 2순위: 개최 예정 (현재 < 시작일) 중 시작일 제일 가까운 순
    @Query("SELECT f FROM Festival f WHERE f.startAt > :now ORDER BY f.startAt ASC")
    List<Festival> findUpcoming(@Param("now") LocalDateTime now, Pageable pageable);

    // 3순위: 이미 종료 (종료일 < 현재) 중 종료일 제일 가까운 순
    @Query("SELECT f FROM Festival f WHERE f.endAt < :now ORDER BY f.endAt DESC")
    List<Festival> findEnd(@Param("now") LocalDateTime now, Pageable pageable);

}
