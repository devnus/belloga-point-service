package com.devnus.belloga.point.point.repository;

import com.devnus.belloga.point.point.domain.TempPoint;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempPointRepository extends JpaRepository<TempPoint, Long> {
    @EntityGraph(value = "TempPoint.point")
    Optional<TempPoint> findByLabelingUUID(String labelingUUID);
}