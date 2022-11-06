package com.devnus.belloga.point.gift.repository;

import com.devnus.belloga.point.gift.domain.ApplyGift;
import com.devnus.belloga.point.gift.domain.ApplyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplyGiftRepository extends JpaRepository<ApplyGift, Long> {
    Page<ApplyGift> findByLabelerId(Pageable pageable, String LabelerId);
    Page<ApplyGift> findByGiftIdAndApplyStatus(Pageable pageable, Long giftId, ApplyStatus applyStatus);

    @Modifying(clearAutomatically = true)
    @Query("update ApplyGift ag SET ag.applyStatus = :applyStatus where ag.id in :ids")
    void bulkUpdateToChangeStatus(@Param("ids") List<Long> ids, @Param("applyStatus") ApplyStatus applyStatus);
}