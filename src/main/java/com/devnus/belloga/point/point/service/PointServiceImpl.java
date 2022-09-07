package com.devnus.belloga.point.point.service;

import com.devnus.belloga.point.common.exception.error.NotFoundLabelerIdException;
import com.devnus.belloga.point.common.exception.error.NotFoundTempPointException;
import com.devnus.belloga.point.point.domain.Point;
import com.devnus.belloga.point.point.domain.TempPoint;
import com.devnus.belloga.point.point.domain.TempPointStatus;
import com.devnus.belloga.point.point.event.ResponsePoint;
import com.devnus.belloga.point.point.repository.PointRepository;
import com.devnus.belloga.point.point.repository.TempPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;
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

    /**
     * 라벨링UUID 에 해당하는 임시 포인트를 실제 포인트로 변환한다.
     * @param labelingUUID
     */
    @Transactional
    @Override
    public void changeTmpPointToPoint(String labelingUUID) {
        TempPoint tempPoint = tempPointRepository.findByLabelingUUID(labelingUUID)
                .orElseThrow(()->new NotFoundTempPointException());

        // 임시포인트를 실제 포인트로 변환한다.
        tempPoint.changeTmpPointToPoint();
    }

    /**
     * 라벨링UUID 에 해당하는 임시 포인트를 블럭한다.
     * @param labelingUUID
     */
    @Transactional
    @Override
    public void deleteTmpPoint(String labelingUUID) {
        TempPoint tempPoint = tempPointRepository.findByLabelingUUID(labelingUUID)
                .orElseThrow(()->new NotFoundTempPointException());

        tempPoint.changeTmpPointStatus(TempPointStatus.BLOCKED);
    }

    /**
     * 나의 임시포인트들의 합과 포인트를 반환한다.
     * @param labelerId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public ResponsePoint.MyPointInfo getMyPointInfo(String labelerId) {
        Point point = pointRepository.findByLabelerId(labelerId)
                .orElseThrow(()->new NotFoundLabelerIdException());
        Long sumTempPoint = tempPointRepository.getSumTempPointByLabelerId(labelerId);

        return ResponsePoint.MyPointInfo.builder()
                .pointValue(point.getPointValue())
                .tempPointValue(sumTempPoint)
                .build();
    }
}
