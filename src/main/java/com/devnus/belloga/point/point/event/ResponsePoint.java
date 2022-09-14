package com.devnus.belloga.point.point.event;

import lombok.Builder;
import lombok.Data;

public class ResponsePoint {
    @Builder
    @Data
    public static class MyPointInfo {
        private Long pointValue;
        private Long tempPointValue;
    }
}
