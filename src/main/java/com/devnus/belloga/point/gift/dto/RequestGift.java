package com.devnus.belloga.point.gift.dto;

import com.devnus.belloga.point.gift.domain.GiftType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class RequestGift {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateGift {
        @NotNull(message = "giftType 이 비어있음")
        private GiftType giftType;
        @NotEmpty(message = "타이틀이 비어있음")
        private String title;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date expectedDrawDate; // 추첨날짜
    }
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateGifticon {
        @NotNull(message = "기프티콘 아이디가 비어있음")
        private Long giftId;
        @NotEmpty(message = "기프티콘 title이 비어있음")
        private String title;
        @NotEmpty(message = "기프티콘 code가 비어있음")
        private String code;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date expectedDate;
    }
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnterGift {
        @NotNull(message = "지원 할 gift 가 비어있음")
        private Long giftId;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DrawGift {
        @NotNull(message = "gift id가 비어있음")
        private Long giftId;
        @NotNull(message = "gift type이 비어있음")
        private GiftType giftType;
    }
}
