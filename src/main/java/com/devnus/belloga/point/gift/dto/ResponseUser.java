package com.devnus.belloga.point.gift.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ResponseUser {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LabelerInfo {
        private String phoneNumber;
        private String email;
        private String name;
        private String birthYear;
    }
}
