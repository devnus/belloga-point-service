package com.devnus.belloga.point.gift.dto;

import com.devnus.belloga.point.gift.domain.ApplyGift;
import com.devnus.belloga.point.gift.domain.ApplyStatus;
import com.devnus.belloga.point.gift.domain.Gift;
import com.devnus.belloga.point.gift.domain.GiftType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class ResponseGift {

    @Builder
    @Data
    public static class GiftProject {
        private String title;
        private GiftType giftType;
        private Date expectedDrawDate;
        private float odds; // 당첨확률

        public static GiftProject of(Gift gift, float odds) {
            return GiftProject.builder()
                    .title(gift.getTitle())
                    .giftType(gift.getGiftType())
                    .expectedDrawDate(gift.getExpectedDrawDate())
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
