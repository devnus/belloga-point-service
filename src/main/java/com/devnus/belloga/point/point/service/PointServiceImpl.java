package com.devnus.belloga.point.point.service;

import com.devnus.belloga.point.point.domain.Point;
import com.devnus.belloga.point.point.domain.TempPoint;
import com.devnus.belloga.point.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;

    /**
     * 임시포인트를 적립한다.
     * @param labelerId
     * @param labelingUUID
     * @param value
     */
    @Transactional
    @Override
    public void saveTempPoint(String labelerId, String labelingUUID, Long value) {
        Point point = pointRepository.findByLabelerId(labelerId).orElseGet(()->null);
        if(point == null) {
            point = pointRepository.save(Point.builder()
                            .labelerId(labelerId)
                            .pointValue(0L)
                    .build());
        }
        point.addTempPoint(TempPoint.builder()
                        .pointValue(value)
                        .labelingUUID(labelingUUID)
                .build());
    }
}
