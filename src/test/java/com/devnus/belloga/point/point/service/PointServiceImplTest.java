package com.devnus.belloga.point.point.service;


import com.devnus.belloga.point.common.exception.error.NotFoundTempPointException;
import com.devnus.belloga.point.point.domain.Point;
import com.devnus.belloga.point.point.domain.TempPoint;
import com.devnus.belloga.point.point.domain.TempPointStatus;
import com.devnus.belloga.point.point.dto.EventLabeledData;
import com.devnus.belloga.point.point.repository.PointRepository;
import com.devnus.belloga.point.point.repository.TempPointRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest // JPA관련 의존성만 가져옴
public class PointServiceImplTest {

    PointServiceImpl pointService;

    @Autowired
    PointRepository pointRepository;
    @Autowired
    TempPointRepository tempPointRepository;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); //Mock어노테이션 필드 초기화
        pointService = new PointServiceImpl(pointRepository, tempPointRepository);
    }

    @AfterEach
    void closeMock() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

    @Transactional
    @Test
    @DisplayName("임시포인트 지급 테스트")
    void saveTempPointTest() {
        // given
        String labelerId = "labeler-hello";
        EventLabeledData.PayTmpPointToLabeler event = EventLabeledData.PayTmpPointToLabeler.builder()
                .labelerId(labelerId)
                .labelingUUID("label1")
                .value(15L)
                .build();
        //when
        pointService.saveTempPoint(event.getLabelerId(), event.getLabelingUUID(), event.getValue());
        //then
        Point point = pointRepository.findByLabelerId(labelerId).orElseGet(()->null);
        System.out.println(point.getPointValue());
        List<TempPoint> list = point.getTempPointList();
        assertEquals(list.size(), 1);
    }

    @Transactional
    @Test
    @DisplayName("임시포인트를 포인트로 변환 테스트")
    void changeTmpPointToPointTest() {
        // given
        String labelerId = "labeler-hello";
        String labelingUUID = "label1";
        EventLabeledData.PayTmpPointToLabeler event = EventLabeledData.PayTmpPointToLabeler.builder()
                .labelerId(labelerId)
                .labelingUUID(labelingUUID)
                .value(15L)
                .build();
        pointService.saveTempPoint(event.getLabelerId(), event.getLabelingUUID(), event.getValue());

        // when
        pointService.changeTmpPointToPoint(labelingUUID);
        // then

        TempPoint tempPoint = tempPointRepository.findByLabelingUUID(labelingUUID).orElseThrow(()->new NotFoundTempPointException());
        assertEquals(tempPoint.getStatus(), TempPointStatus.CHANGED);
        Point point = pointRepository.findByLabelerId(labelerId).orElseThrow(()->new NotFoundTempPointException());
        assertEquals(point.getPointValue(), 15L);
    }
    @Transactional
    @Test
    @DisplayName("임시포인트를 회수 테스트")
    void blockTempPointTest() {
        // given
        String labelerId = "labeler-hello";
        String labelingUUID = "label1";
        EventLabeledData.PayTmpPointToLabeler event = EventLabeledData.PayTmpPointToLabeler.builder()
                .labelerId(labelerId)
                .labelingUUID(labelingUUID)
                .value(15L)
                .build();
        pointService.saveTempPoint(event.getLabelerId(), event.getLabelingUUID(), event.getValue());

        // when
        pointService.deleteTmpPoint(labelingUUID);

        // then
        TempPoint tempPoint = tempPointRepository.findByLabelingUUID(labelingUUID).orElseThrow(()->new NotFoundTempPointException());
        assertEquals(tempPoint.getStatus(), TempPointStatus.BLOCKED);
    }
}
