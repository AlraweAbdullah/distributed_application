package be.ucll.project.reservationservice.messaging;


import be.ucll.project.reservationservice.client.car.api.model.CheckCarExistCommand;
import be.ucll.project.reservationservice.client.car.api.model.CreateReservationCommand;
import be.ucll.project.reservationservice.client.car.api.model.DeclineReservationCommand;
import be.ucll.project.reservationservice.client.notification.api.model.SendEmailCommand;
import be.ucll.project.reservationservice.client.user.api.model.ValidateUserCommand;
import be.ucll.project.reservationservice.domain.reservation.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RabbitMqMessageSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMqMessageSender.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendValidateUserCommand(Long reservationId, String userEmail) {
        var command = new ValidateUserCommand();
        command.userEmail(userEmail);
        command.reservationId(reservationId);
        sendToQueue("q.user-service.validate-user", command);
    }

    public void sendValidateCarCommand(Long reservationID, Long carId) {
        var command = new CheckCarExistCommand();
        command.reservationId(reservationID);
        command.carId(carId);
        sendToQueue("q.car-service.check-car-exist", command);
    }

    public void sendEmail(String recipient, String message) {
        var command = new SendEmailCommand();
        command.recipient(recipient);
        command.message(message);
        sendToQueue("q.notification-service.send-email", command);
    }

    public void sendCreateReservationCommand(Reservation reservation) {
        var event = new CreateReservationCommand();
        event.setReservationId(reservation.getId());
        event.setCarId(reservation.getCarId());
        event.setStartDate(reservation.getStartDate());
        event.setEndDate(reservation.getEndDate());

        sendToQueue("q.car-service.reservation-created", event);
    }

    public void sendReservationDeclinedEvent(Reservation reservation) {
        var event = new DeclineReservationCommand();
        event.setReservationId(reservation.getId());
        sendToQueue("q.car-service.reservation-declined", event);
    }

    private void sendToQueue(String queue, Object message) {
        LOGGER.info("Sending message: " + message);

        this.rabbitTemplate.convertAndSend(queue, message);
    }
}
