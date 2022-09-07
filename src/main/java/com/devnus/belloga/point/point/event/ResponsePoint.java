package com.devnus.belloga.point.point.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ResponsePoint {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPointInfo {
        private Long pointValue;
        private Long tempPointValue;
    }
}
