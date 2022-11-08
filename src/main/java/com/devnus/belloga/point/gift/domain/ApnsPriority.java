package com.devnus.belloga.point.gift.domain;

import lombok.Getter;

@Getter
public enum ApnsPriority {
    FIVE("5"),
    TEN("10");

    private String value;
    ApnsPriority(String value) {
        this.value = value;
    }
}
