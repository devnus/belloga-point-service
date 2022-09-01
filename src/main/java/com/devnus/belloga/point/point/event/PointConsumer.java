package com.devnus.belloga.point.point.event;

import com.devnus.belloga.point.point.dto.EventLabeledData;
import com.devnus.belloga.point.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 포인트 컨슈머를 처리한다.
 */
@Component
@RequiredArgsConstructor
public class PointConsumer {
    private static final String PAY_TEMP_POINT_TO_LABELER_TOPIC = "pay-tmp-point-to-labeler";
    private static final String PAY_TEMP_POINT_TO_LABELER_TOPIC_GROUP = "pay-tmp-point-to-labeler-1";
    private static final String CHANGE_TMP_POINT_TO_POINT = "change-tmp-point-to-point";
    private static final String CHANGE_TMP_POINT_TO_POINT_GROUP = "change-tmp-point-to-point-1";
    private static final String DELETE_TMP_POINT = "delete-tmp-point";
    private static final String DELETE_TMP_POINT_GROUP = "delete-tmp-point-1";
    private final PointService pointService;

    /**
     * 라벨링 마이크로서비스로부터 임시 포인트 지급 이벤트를 받아 처리한다
     * @param event
     * @throws IOException
     */
    @KafkaListener(topics = PAY_TEMP_POINT_TO_LABELER_TOPIC, groupId = PAY_TEMP_POINT_TO_LABELER_TOPIC_GROUP, containerFactory = "eventLabeledDataPayTmpPointToLabelerListener")
    protected void consumePayTmpPointToLabeler(EventLabeledData.PayTmpPointToLabeler event) throws IOException {
        pointService.saveTempPoint(event.getLabelerId(), event.getLabelingUUID(), event.getValue());
    }

    /**
     * 임시 포인트를 실제 포인트로 변환하는 이벤트 처리
     * @param event
     * @throws IOException
     */
    @KafkaListener(topics = CHANGE_TMP_POINT_TO_POINT, groupId = CHANGE_TMP_POINT_TO_POINT_GROUP, containerFactory = "eventLabeledDataChangeTmpPointToPointListener")
    protected void consumeChangeTmpPointToPointEvent(EventLabeledData.ChangeTmpPointToPoint event) throws IOException {
        pointService.changeTmpPointToPoint(event.getLabelingUUID());
    }

    /**
     * 임시 포인트를 삭제하는 이벤트 처리
     * @param event
     * @throws IOException
     */
    @KafkaListener(topics = DELETE_TMP_POINT, groupId = DELETE_TMP_POINT_GROUP, containerFactory = "eventLabeledDataDeleteTmpPointListener")
    protected void consumeDeleteTmpPointEvent(EventLabeledData.DeleteTmpPoint event) throws IOException {
        pointService.deleteTmpPoint(event.getLabelingUUID());
    }
}
