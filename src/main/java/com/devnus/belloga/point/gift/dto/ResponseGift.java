package com.devnus.belloga.point.gift.dto;

import com.devnus.belloga.point.gift.domain.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

public class ResponseGift {

    @Builder
    @Data
    public static class GiftProject {
        private Long id;
        private String title;
        private GiftType giftType;
        private Date expectedDrawDate;
        private GiftStatus giftStatus;
        private float odds; // 당첨확률

        public static GiftProject of(Gift gift, float odds) {
            return GiftProject.builder()
                    .id(gift.getId())
                    .title(gift.getTitle())
                    .giftType(gift.getGiftType())
                    .expectedDrawDate(gift.getExpectedDrawDate())
                    .giftStatus(gift.getGiftStatus())
                    .odds(odds)
                    .build();
        }
    }

    @Builder
    @Data
    public static class ApplyGiftInfo {
        private String title;
        private GiftType giftType;
        private Date expectedDrawDate;
        private ApplyStatus applyStatus; //당첨 여부

        public static ApplyGiftInfo of(ApplyGift applyGift) {
            return ApplyGiftInfo.builder()
                    .title(applyGift.getGift().getTitle())
                    .giftType(applyGift.getGift().getGiftType())
                    .expectedDrawDate(applyGift.getGift().getExpectedDrawDate())
                    .applyStatus(applyGift.getApplyStatus()).build();
        }
    }
}
