package com.waglewagle.server.domain.festivalMap.repository;

import com.waglewagle.server.domain.festivalMap.entity.FestivalMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FestivalMapRepository extends JpaRepository<FestivalMap, Long> {
    List<FestivalMap> findByFestivalId(Long festivalId);

    List<FestivalMap> findByFestivalIdOrderBySequenceAsc(Long festivalId);
}
