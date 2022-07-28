package com.devnus.belloga.point.tempPoint.service;


import com.devnus.belloga.point.tempPoint.domain.TempPoint;
import com.devnus.belloga.point.tempPoint.dto.EventLabeledData;
import com.devnus.belloga.point.tempPoint.repository.TempPointRepository;
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
public class TempPointServiceImplTest {

    TempPointServiceImpl tempPointService;

    @Autowired
    TempPointRepository tempPointRepository;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); //Mock어노테이션 필드 초기화
        tempPointService = new TempPointServiceImpl(tempPointRepository);
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
        EventLabeledData.PayTmpPointToLabeler event = EventLabeledData.PayTmpPointToLabeler.builder()
                .labelerId("labeler-hello")
                .labelingUUID("label1")
                .value(15L)
                .build();
        //when
        tempPointService.saveTempPoint(event.getLabelerId(), event.getLabelingUUID(), event.getValue());
        //then
        List<TempPoint> list = tempPointRepository.findAll();
        assertEquals(list.size(), 1);
    }
}
