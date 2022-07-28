package com.devnus.belloga.point.tempPoint.service;

import com.devnus.belloga.point.tempPoint.domain.TempPoint;
import com.devnus.belloga.point.tempPoint.repository.TempPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TempPointServiceImpl implements TempPointService {
    private final TempPointRepository tempPointRepository;

    /**
     * 임시포인트를 적립한다.
     * @param labelerId
     * @param labelingUUID
     * @param value
     */
    @Transactional
    @Override
    public void saveTempPoint(String labelerId, String labelingUUID, Long value) {
        tempPointRepository.save(TempPoint.builder()
                        .labelerId(labelerId)
                        .labelingUUID(labelingUUID)
                        .pointValue(value)
                .build());
    }
}
