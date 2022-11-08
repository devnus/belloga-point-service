package com.devnus.belloga.point.gift.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiftProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value(value = "${app.topic.cloud-messaging-token.fcm-push-notification-to-token}")
    private String FCM_PUSH_NOTIFICATION_TO_TOKEN;

    public void winNotice(Object event) {
        kafkaTemplate.send(FCM_PUSH_NOTIFICATION_TO_TOKEN, event);
    }

}
