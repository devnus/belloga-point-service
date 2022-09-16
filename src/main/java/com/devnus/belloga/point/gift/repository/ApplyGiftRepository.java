package com.devnus.belloga.point.gift.repository;

import com.devnus.belloga.point.gift.domain.ApplyGift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyGiftRepository extends JpaRepository<ApplyGift, Long> {
    Page<ApplyGift> findByLabelerId(Pageable pageable, String LabelerId);
}
