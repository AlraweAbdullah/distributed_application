package be.ucll.project.reservationservice.messaging;


import be.ucll.project.reservationservice.client.car.api.model.CarExistedEvent;
import be.ucll.project.reservationservice.client.user.api.model.UserValidatedEvent;
import be.ucll.project.reservationservice.domain.reservation.ReservationRequestSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private final ReservationRequestSaga saga;

    @Autowired
    public MessageListener(ReservationRequestSaga saga) {
        this.saga = saga;
    }

    @RabbitListener(queues = {"q.user-validated.reservation-service"})
    public void onUserValidated(UserValidatedEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.saga.executeSaga(event.getReservationId(), event);
    }

    @RabbitListener(queues = {"q.car-exist.reservation-service"})
    public void onCarIsFree(CarExistedEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.saga.executeSaga(event.getReservationId(), event);
    }

}