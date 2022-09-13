package com.devnus.belloga.point.gift.dto;

import com.devnus.belloga.point.gift.domain.Gift;
import com.devnus.belloga.point.gift.domain.GiftType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

public class ResponseGift {

    @Builder
    @Data
    public static class GiftProject {
        private String title;
        private GiftType giftType;
        private Date expectedDrawDate;

        public static GiftProject of(Gift gift) {
            return GiftProject.builder()
                    .title(gift.getTitle())
                    .giftType(gift.getGiftType())
                    .expectedDrawDate(gift.getExpectedDrawDate())
                    .build();
        }
    }
}
