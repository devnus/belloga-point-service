package com.devnus.belloga.point.stamp.service;

import com.devnus.belloga.point.stamp.dto.ResponseStamp;

public interface StampService {
    void addStamp(String labelerId);
    ResponseStamp.MyStampInfo getMyStampInfo(String labelerId);
}
