package com.devnus.belloga.point.stamp.service;

import com.devnus.belloga.point.common.exception.error.InsufficientPointException;
import com.devnus.belloga.point.common.exception.error.NotFoundLabelerIdException;
import com.devnus.belloga.point.point.domain.Point;
import com.devnus.belloga.point.point.repository.PointRepository;
import com.devnus.belloga.point.stamp.domain.Stamp;
import com.devnus.belloga.point.stamp.repository.StampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StampServiceImpl implements StampService {
    private final StampRepository stampRepository;
    private final PointRepository pointRepository;

    /**
     * 포인트를 소비해 스탬프를 찍는다
     * @param labelerId
     */
    @Override
    @Transactional
    public void addStamp(String labelerId) {
        Long ratio = 10L; // 교환비율은 상의해야함

        Point point = pointRepository.findByLabelerId(labelerId)
                .orElseThrow(()->new NotFoundLabelerIdException());
        if(point.getPointValue() - ratio < 0) throw new InsufficientPointException();

        Stamp stamp = stampRepository.findByLabelerId(labelerId)
                .orElseThrow(()->new NotFoundLabelerIdException());

        point.decreasePoint(ratio); // 포인트 감소시킨다.
        stamp.increaseStamp(1); // 개수를 1 증가시킨다.
    }
}
