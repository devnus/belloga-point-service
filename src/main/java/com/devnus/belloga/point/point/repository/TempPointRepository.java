package com.devnus.belloga.point.point.repository;

import com.devnus.belloga.point.point.domain.TempPoint;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TempPointRepository extends JpaRepository<TempPoint, Long> {
    @EntityGraph(value = "TempPoint.point")
    Optional<TempPoint> findByLabelingUUID(String labelingUUID);
    @Query(name = "TempPoint.sumTempPointByLabelerId")
    Long getSumTempPointByLabelerId(@Param("labelerId") String labelerId);
}
