package be.ucll.project.carservice.messaging;

import be.ucll.project.carservice.client.owner.api.model.OwnerCreatedEvent;
import be.ucll.project.carservice.domain.owner.Owner;
import be.ucll.project.carservice.domain.owner.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OwnerCreatedEventListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(OwnerCreatedEventListener.class);

    private final OwnerRepository repository;

    @Autowired
    public OwnerCreatedEventListener(OwnerRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = {"q.owner-created"})
    public void onOwnerCreated(OwnerCreatedEvent event) {
        Owner owner = new Owner(event.getOwner().getId(), event.getOwner().getEmail());
        LOGGER.info("Received ownerCreatedEvent..." + owner);
        this.repository.save(owner);
    }

}
