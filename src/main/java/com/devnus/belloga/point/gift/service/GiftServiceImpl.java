package com.devnus.belloga.point.gift.service;

import com.devnus.belloga.point.common.exception.error.InsufficientStampException;
import com.devnus.belloga.point.common.exception.error.NotFoundGiftIdException;
import com.devnus.belloga.point.common.exception.error.NotFoundLabelerIdException;
import com.devnus.belloga.point.gift.domain.ApplyGift;
import com.devnus.belloga.point.gift.domain.Gift;
import com.devnus.belloga.point.gift.domain.GiftType;
import com.devnus.belloga.point.gift.dto.ResponseGift;
import com.devnus.belloga.point.gift.repository.ApplyGiftRepository;
import com.devnus.belloga.point.gift.repository.GiftRepository;
import com.devnus.belloga.point.gift.repository.GifticonRepository;
import com.devnus.belloga.point.stamp.domain.Stamp;
import com.devnus.belloga.point.stamp.repository.StampRepository;
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
    private final StampRepository stampRepository;
    private final ApplyGiftRepository applyGiftRepository;

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
     * 생성된 모든 이벤트를 조회한다. 당첨 확률도 반환한다.
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResponseGift.GiftProject> getAllGiftProject(Pageable pageable) {
        Page<Gift> list = giftRepository.findAll(pageable);
        return list.map((gift) -> {
            // 당첨확률은 gifticon 수 / 응모자 수 로 해당 gift 이벤트에 응모 시 당첨확률 구하기
            float odds = 0;
            if(gift.getGiftType().equals(GiftType.GIFTICON)) {
                System.out.println(gift.getGifticonList().size());
                System.out.println(gift.getApplyGiftList().size());
                odds = gift.getGifticonList().size() / (float) (gift.getApplyGiftList().size() == 0 ? 1 : gift.getApplyGiftList().size());
            }
            if(odds > 1) odds = 1;
            return ResponseGift.GiftProject.of(gift, odds);
            }
        );
    }

    /**
     * 이벤트 응모
     */
    @Override
    @Transactional
    public boolean createApplyGift(String labelerId, Long giftId) {
        Gift gift = giftRepository.findById(giftId).orElseThrow(() -> new NotFoundGiftIdException());
        Stamp labelerStamp = stampRepository.findByLabelerId(labelerId).orElseThrow(() -> new NotFoundLabelerIdException());

        //스탬프가 10개 미만으로 있을때
        if(labelerStamp.getStampValue() < 10){
            throw new InsufficientStampException();
        }

        //기프티콘 응모시 스템프 10개 감소
        labelerStamp.decreaseStamp(10);

        //응모 저장
        ApplyGift applyGift = ApplyGift.builder()
                .gift(gift)
                .labelerId(labelerId).build();
        applyGiftRepository.save(applyGift);

        return true;
    }

    /**
     * 라벨러가 응모한 내용 조회
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResponseGift.ApplyGiftInfo> findApplyGiftInfoByLabelerId(Pageable pageable, String labelerId) {
        Page<ApplyGift> applyGifts = applyGiftRepository.findByLabelerId(pageable, labelerId);
        Page<ResponseGift.ApplyGiftInfo> applyGiftInfos = applyGifts.map((ApplyGift applyGift) -> ResponseGift.ApplyGiftInfo.of(applyGift));
        return applyGiftInfos;
    }
}
