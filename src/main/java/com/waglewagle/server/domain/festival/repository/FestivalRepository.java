package com.waglewagle.server.domain.festival.repository;

import com.waglewagle.server.domain.festival.entity.Festival;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FestivalRepository  extends JpaRepository<Festival, Long> {

    Page<Festival> findByNameContaining(String keyword, Pageable pageable);
}
