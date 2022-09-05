package com.devnus.belloga.point.stamp.dto;

import com.devnus.belloga.point.stamp.domain.Stamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ResponseStamp {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyStampInfo {
        private Integer stampValue;
        public static MyStampInfo of(Stamp stamp) {
            return MyStampInfo.builder()
                    .stampValue(stamp.getStampValue())
                    .build();
        }
    }
}
