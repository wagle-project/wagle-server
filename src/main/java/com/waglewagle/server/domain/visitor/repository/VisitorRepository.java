package com.waglewagle.server.domain.visitor.repository;

import com.waglewagle.server.domain.visitor.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
}
