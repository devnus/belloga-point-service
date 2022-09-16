package com.devnus.belloga.point.gift.service;

import com.devnus.belloga.point.gift.domain.GiftType;
import com.devnus.belloga.point.gift.dto.ResponseGift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface GiftService {
    void createGiftProject(String adminId, GiftType giftType, String title, Date expectedDrawDate);
    Page<ResponseGift.GiftProject> getAllGiftProject(Pageable pageable);
    boolean createApplyGift(String labelerId, Long giftId);
    Page<ResponseGift.ApplyGiftInfo> findApplyGiftInfoByLabelerId(Pageable pageable, String labelerId);
}
