package courseEnrollement.example.demo.service.impl;

import courseEnrollement.example.demo.service.NotificationService;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class SmsNotificationService implements NotificationService {

    @Override
    public void sendNotification(String message) {
        System.out.println("SMS notification: " + message);
    }
}
