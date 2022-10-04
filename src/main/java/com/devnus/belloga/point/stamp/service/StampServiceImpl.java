package com.devnus.belloga.point.stamp.service;

import com.devnus.belloga.point.common.exception.error.InsufficientPointException;
import com.devnus.belloga.point.common.exception.error.NotFoundLabelerIdException;
import com.devnus.belloga.point.point.domain.Point;
import com.devnus.belloga.point.point.repository.PointRepository;
import com.devnus.belloga.point.stamp.domain.Stamp;
import com.devnus.belloga.point.stamp.dto.ResponseStamp;
import com.devnus.belloga.point.stamp.repository.StampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StampServiceImpl implements StampService {
    private final StampRepository stampRepository;
    private final PointRepository pointRepository;

    private final long pointRatio; // application 설정파일에서 포인트 교환비율 설정
    public StampServiceImpl(final StampRepository stampRepository,
                            final PointRepository pointRepository,
                            @Value("${app.stamp.point-ratio}")final long pointRatio) {

        this.stampRepository = stampRepository;
        this.pointRepository = pointRepository;
        this.pointRatio = pointRatio;
    }

    /**
     * 포인트를 소비해 스탬프를 찍는다
     * @param labelerId
     */
    @Override
    @Transactional
    public void addStamp(String labelerId) {
        Point point = pointRepository.findByLabelerId(labelerId)
                .orElseThrow(()->new NotFoundLabelerIdException());
        if(point.getPointValue() - pointRatio < 0) throw new InsufficientPointException();

        Stamp stamp = stampRepository.findByLabelerId(labelerId)
                .orElseGet(null);

        if(stamp == null) {
            stamp = stampRepository.save(Stamp.builder()
                            .labelerId(labelerId)
                    .build());
        }

        point.decreasePoint(pointRatio); // 포인트 감소시킨다.
        stamp.increaseStamp(1); // 개수를 1 증가시킨다.
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseStamp.MyStampInfo getMyStampInfo(String labelerId) {
        Stamp stamp = stampRepository.findByLabelerId(labelerId)
                .orElseThrow(()->new NotFoundLabelerIdException());
        return ResponseStamp.MyStampInfo.of(stamp);
    }
}
