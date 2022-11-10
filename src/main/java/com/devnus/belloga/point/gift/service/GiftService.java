package com.devnus.belloga.point.gift.service;

import com.devnus.belloga.point.gift.domain.GiftType;
import com.devnus.belloga.point.gift.dto.ResponseGift;
import com.devnus.belloga.point.gift.dto.ResponseUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface GiftService {
    void createGiftProject(String adminId, GiftType giftType, String title, Date expectedDrawDate);
    void addGifticonToGiftProject(Long giftId, String title, String code, Date expiredDate);
    Page<ResponseGift.GiftProject> getAllGiftProject(Pageable pageable);
    boolean createApplyGift(String labelerId, Long giftId);
    Page<ResponseGift.ApplyGiftInfo> findApplyGiftInfoByLabelerId(Pageable pageable, String labelerId);
    void drawGifticonEvent(String adminId, Long giftId);
    Page<ResponseUser.LabelerInfo> findGiftWinners(Pageable pageable, Long giftId);
}
