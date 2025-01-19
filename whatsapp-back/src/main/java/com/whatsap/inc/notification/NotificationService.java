package com.whatsap.inc.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String userId, Notification notification) {
        log.info("Sending notification to {}", userId);
        messagingTemplate.convertAndSendToUser(userId,
                "/chat",
                notification);
    }

}
