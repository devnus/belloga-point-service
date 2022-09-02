package com.devnus.belloga.point.point.service;

public interface PointService {
    void saveTempPoint(String labelerId, String labelingUUID, Long value);
    void changeTmpPointToPoint(String labelingUUID);
    void deleteTmpPoint(String labelingUUID);
}
