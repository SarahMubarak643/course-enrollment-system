package courseEnrollement.example.demo.service.impl;

import courseEnrollement.example.demo.service.NotificationService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class EmailNotificationService implements NotificationService {

    @Override
    public void sendNotification(String message) {
        System.out.println("Email notification: " + message);
    }
}
