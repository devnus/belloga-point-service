package com.devnus.belloga.point.gift.repository;

import com.devnus.belloga.point.gift.domain.Gift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftRepository extends JpaRepository<Gift, Long> {
    Page<Gift> findAll(Pageable pageable);
}
