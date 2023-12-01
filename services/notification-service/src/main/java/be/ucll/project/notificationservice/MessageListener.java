package be.ucll.project.notificationservice;

import be.ucll.project.notificationservice.api.messaging.model.SendEmailCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    @RabbitListener(queues = {"q.notification-service.send-email"})
    public void onSendEmail(SendEmailCommand command) {
        LOGGER.info("Sending email with text " + command.getMessage() +
                "to recipient "+ command.getRecipient() + ".");
    }
}
