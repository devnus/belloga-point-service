package com.devnus.belloga.point.tempPoint.event;

import com.devnus.belloga.point.tempPoint.dto.EventLabeledData;
import com.devnus.belloga.point.tempPoint.service.TempPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 포인트 컨슈머를 처리한다.
 */
@Component
@RequiredArgsConstructor
public class TempPointConsumer {
    private static final String PAY_TEMP_POINT_TO_LABELER_TOPIC = "pay-tmp-point-to-labeler";
    private static final String PAY_TEMP_POINT_TO_LABELER_TOPIC_GROUP = "pay-tmp-point-to-labeler-1";
    private final TempPointService tempPointService;

    /**
     * 라벨링 마이크로서비스로부터 임시 포인트 지급 이벤트를 받아 처리한다
     * @param event
     * @throws IOException
     */
    @KafkaListener(topics = PAY_TEMP_POINT_TO_LABELER_TOPIC, groupId = PAY_TEMP_POINT_TO_LABELER_TOPIC_GROUP)
    protected void consumePayTmpPointToLabeler(Object event) throws IOException {
        EventLabeledData.PayTmpPointToLabeler consumeEvent = (EventLabeledData.PayTmpPointToLabeler) event;
        tempPointService.saveTempPoint(consumeEvent.getLabelerId(), consumeEvent.getLabelingUUID(), consumeEvent.getValue());
    }
}
