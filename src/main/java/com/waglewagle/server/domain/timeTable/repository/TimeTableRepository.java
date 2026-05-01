package com.waglewagle.server.domain.timeTable.repository;

import com.waglewagle.server.domain.timeTable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    List<TimeTable> findByFestivalIdOrderBySequenceAsc(Long festivalId);
}
