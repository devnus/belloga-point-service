package com.devnus.belloga.point.gift.repository;

import com.devnus.belloga.point.gift.domain.ApplyGift;
import com.devnus.belloga.point.gift.domain.ApplyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplyGiftRepository extends JpaRepository<ApplyGift, Long> {
    Page<ApplyGift> findByLabelerId(Pageable pageable, String LabelerId);

    @Modifying(clearAutomatically = true)
    @Query("update ApplyGift ag SET ag.applyStatus = :applyStatus where ag.id in :ids")
    void bulkUpdateToChangeStatus(List<Long> ids, ApplyStatus applyStatus);
}