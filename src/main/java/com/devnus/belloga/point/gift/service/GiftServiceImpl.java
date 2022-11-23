package com.devnus.belloga.point.gift.service;

import com.devnus.belloga.point.common.exception.error.InsufficientDrawConditionException;
import com.devnus.belloga.point.common.exception.error.InsufficientStampException;
import com.devnus.belloga.point.common.exception.error.NotFoundGiftIdException;
import com.devnus.belloga.point.common.exception.error.NotFoundLabelerIdException;
import com.devnus.belloga.point.gift.domain.*;
import com.devnus.belloga.point.gift.dto.EventCloudMessagingToken;
import com.devnus.belloga.point.gift.dto.ResponseGift;
import com.devnus.belloga.point.gift.dto.ResponseUser;
import com.devnus.belloga.point.gift.event.GiftProducer;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GiftServiceImpl implements GiftService {
    private final GiftRepository giftRepository;
    private final GifticonRepository gifticonRepository;
    private final StampRepository stampRepository;
    private final ApplyGiftRepository applyGiftRepository;
    private final GiftProducer giftProducer;
    private final UserWebClient userWebClient;

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
     * 기프트Id에 기프티콘 정보를 추가한다
     * @param giftId
     * @param title
     * @param code
     */
    @Override
    @Transactional
    public void addGifticonToGiftProject(Long giftId, String title, String code, Date expiredDate) {
        Gift gift = giftRepository.findById(giftId)
                .orElseThrow(()->new NotFoundGiftIdException());
        // 기프트타입이 일치하지 않아도 해당 기프트를 찾을 수 없다고 표시
        if(!gift.getGiftType().equals(GiftType.GIFTICON)) throw new NotFoundGiftIdException();

        gift.addGifticon(Gifticon.builder()
                        .expiredDate(expiredDate)
                        .title(title)
                        .code(code)
                .build());
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

        //스탬프가 8개 미만으로 있을때
        if(labelerStamp.getStampValue() < 8){
            throw new InsufficientStampException();
        }

        //기프티콘 응모시 스템프 8개 감소
        labelerStamp.decreaseStamp(8);

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


    /**
     * 기프티콘 이벤트 추첨을 진행한다. Gift 상태를 Done으로 바꾸고 응모자에게 win, lose를 줌
     * @param giftId
     */
    @Override
    @Transactional
    public void drawGifticonEvent(String adminId, Long giftId) {
        Gift gift = giftRepository.findByIdFetchGifticon(giftId)
                .orElseThrow(()->new NotFoundGiftIdException());

        // 응모자 id 추출
        List<Long> ids = new LinkedList<>();
        gift.getApplyGiftList().forEach(applyGift -> ids.add(applyGift.getId()));

        // 응모자 추첨 조건이 충분치 않다.
        if(ids.size() == 0 || ids.size() < gift.getGifticonList().size())
            throw new InsufficientDrawConditionException();

        // 응모자에게 전부 LOSE를 부여
        applyGiftRepository.bulkUpdateToChangeStatus(ids, ApplyStatus.LOSE);
        //  N명에게만 WIN 부여
        List<Long> wins = new LinkedList<>();
        int count = 0; // 추출한 사람 수
        // 기프티콘 개수만큼 반복문을 돌려 승리자 추출
        for(int i = 0 ; i < gift.getGifticonList().size() ; i++) {
            int randomIdx = (int) (Math.random() * (gift.getApplyGiftList().size() - 1));
            // 이미 추출한 숫자이며 뽑을 사람이 아직 남아있는 경우 다시뽑기
            while (wins.contains(ids.get(randomIdx)) && count < gift.getApplyGiftList().size()) {
                randomIdx = (int) (Math.random() * (gift.getApplyGiftList().size() - 1));
            }
            wins.add(ids.get(randomIdx));
            count++;
        }
        applyGiftRepository.bulkUpdateToChangeStatus(wins, ApplyStatus.WIN);

        // WIN 에게 푸시 알림 보내기
        for(int i = 0; i < wins.size(); i++){

            ApplyGift applyGift  = applyGiftRepository.findById(wins.get(i)).orElseThrow(() -> new NotFoundGiftIdException());

            giftProducer.winNotice(EventCloudMessagingToken.MessageByToken.builder()
                            .androidPriority(AndroidPriority.HIGH)
                            .apnsPriority(ApnsPriority.TEN)
                            .apnsPushType(ApnsPushType.ALERT)
                            .userId(applyGift.getLabelerId())
                            .title("이벤트 추첨 안내")
                            .body("축하합니다 이벤트에 당첨되셨습니다!")
                            .clickLink(" ").build());
        }

        // gift를 done으로 바꾼다.
        gift.changeGiftStatusToDone();
    }

    /**
     * Gift에 대한 당첨자 정보 조회
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResponseUser.LabelerInfo> findGiftWinners(Pageable pageable, Long giftId) {
        Page<ApplyGift> applyGifts = applyGiftRepository.findByGiftIdAndApplyStatus(pageable, giftId, ApplyStatus.WIN);

        Page<ResponseUser.LabelerInfo> giftWinners = applyGifts.map((ApplyGift applyGift) -> {

            //동기 통신을 통해 라벨러 정보를 가져온다
            ResponseUser.LabelerInfo labelerInfo = userWebClient.getLabelerInfo(applyGift.getLabelerId());

            return labelerInfo;
        });

        return giftWinners;
    }
}
