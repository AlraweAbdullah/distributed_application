package be.ucll.project.userservice.messaging;

import be.ucll.project.userservice.api.model.UserValidatedEvent;
import be.ucll.project.userservice.api.model.ValidateUserCommand;
import be.ucll.project.userservice.domain.User;
import be.ucll.project.userservice.domain.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageListener(UserService userService, RabbitTemplate rabbitTemplate) {
        this.userService = userService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = {"q.user-service.validate-user"})
    public void onValidateUser(ValidateUserCommand command) {
        LOGGER.info("Received command: " + command);

        //Doesn't matter if user exists or not
        UserValidatedEvent event = new UserValidatedEvent();
        event.setReservationId(command.getReservationId());

        //Set the event in case user doesn't exist
        event.setIsClient(false);

        User user = userService.validateUser(command.getUserEmail());

        //Set the event in case user exists
        if(user != null){
            event.setIsClient(true);
            event.setUserId(user.getId());
        }
        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.user-validated", "", event);
    }
}
