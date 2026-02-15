package com.waglewagle.server.domain.festival.repository;

import com.waglewagle.server.domain.festival.entity.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository  extends JpaRepository<Festival, Long> {
}
