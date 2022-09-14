package com.devnus.belloga.point.gift.service;

import com.devnus.belloga.point.gift.domain.Gift;
import com.devnus.belloga.point.gift.domain.GiftType;
import com.devnus.belloga.point.gift.dto.ResponseGift;
import com.devnus.belloga.point.gift.repository.GiftRepository;
import com.devnus.belloga.point.gift.repository.GifticonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class GiftServiceImpl implements GiftService {
    private final GiftRepository giftRepository;
    private final GifticonRepository gifticonRepository;
    /**
     * giftType으로 프로젝트를 생성한다.
     * @param adminId
     * @param giftType
     */
    @Override
    @Transactional
    public void createGiftProject(String adminId, GiftType giftType, String title, Date expectedDrawDate) {
        Gift gift = Gift.builder()
                .adminId(adminId)
                .giftType(giftType)
                .title(title)
                .expectedDrawDate(expectedDrawDate)
                .build();
        gift = giftRepository.save(gift);
    }

    /**
     * 생성된 모든 이벤트를 조회한다.
     * @param pageable
     * @return
     */
    @Override
    public Page<ResponseGift.GiftProject> getAllGiftProject(Pageable pageable) {
        Page<Gift> list = giftRepository.findAll(pageable);
        return list.map(ResponseGift.GiftProject::of);
    }
}
