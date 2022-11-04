package com.devnus.belloga.point.gift.domain;

import lombok.Getter;

@Getter
public enum AndroidPriority {
    NORMAL("NORMAL"),
    HIGH("HIGH");

    private String value;
    AndroidPriority(String value) {
        this.value = value;
    }
}
