package com.devnus.belloga.point.gift.dto;

import com.devnus.belloga.point.gift.domain.AndroidPriority;
import com.devnus.belloga.point.gift.domain.ApnsPriority;
import com.devnus.belloga.point.gift.domain.ApnsPushType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EventCloudMessagingToken {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageByToken {
        private String userId; // 사용자 id
        private String title;
        private String body;
        private String clickLink;

        private ApnsPushType apnsPushType;
        private ApnsPriority apnsPriority;
        private AndroidPriority androidPriority;
    }

}
