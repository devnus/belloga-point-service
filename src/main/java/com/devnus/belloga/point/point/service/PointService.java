package com.devnus.belloga.point.point.service;

import com.devnus.belloga.point.point.event.ResponsePoint;

public interface PointService {
    void saveTempPoint(String labelerId, String labelingUUID, Long value);
    void changeTmpPointToPoint(String labelingUUID);
    void deleteTmpPoint(String labelingUUID);
    ResponsePoint.MyPointInfo getMyPointInfo(String labelerId);
}
