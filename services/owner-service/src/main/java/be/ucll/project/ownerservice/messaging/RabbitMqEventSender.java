package be.ucll.project.ownerservice.messaging;

import be.ucll.project.ownerservice.api.model.ApiOwner;
import be.ucll.project.ownerservice.api.model.OwnerCreatedEvent;
import be.ucll.project.ownerservice.domain.EventSender;
import be.ucll.project.ownerservice.domain.Owner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqEventSender implements EventSender {
    private final RabbitTemplate rabbitTemplate;
    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMqEventSender.class);


    @Autowired
    public RabbitMqEventSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendOwnerCreatedEvent(Owner owner) {
        LOGGER.info("Sending message: " + toEvent(owner));

        this.rabbitTemplate.convertAndSend("x.owner-created", "", toEvent(owner));
    }

    private OwnerCreatedEvent toEvent(Owner owner) {
        return new OwnerCreatedEvent()
                .owner(new ApiOwner()
                        .id(owner.getId())
                        .name(owner.getName())
                        .email(owner.getEmail()));
    }
}
