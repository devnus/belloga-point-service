package com.devnus.belloga.point.stamp.repository;

import com.devnus.belloga.point.stamp.domain.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StampRepository extends JpaRepository<Stamp, Long> {
    Optional<Stamp> findByLabelerId(String labelerId);
}
