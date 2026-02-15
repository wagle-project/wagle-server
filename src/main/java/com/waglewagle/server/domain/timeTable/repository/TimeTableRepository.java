package com.waglewagle.server.domain.timeTable.repository;

import com.waglewagle.server.domain.festivalMap.entity.FestivalMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableRepository extends JpaRepository<FestivalMap, Long> {
}
