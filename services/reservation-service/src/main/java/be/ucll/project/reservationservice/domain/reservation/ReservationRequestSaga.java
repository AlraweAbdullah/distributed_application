package be.ucll.project.reservationservice.domain.reservation;


import be.ucll.project.reservationservice.client.car.api.model.CarExistedEvent;
import be.ucll.project.reservationservice.client.user.api.model.UserValidatedEvent;
import be.ucll.project.reservationservice.messaging.RabbitMqMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ReservationRequestSaga {

    private final RabbitMqMessageSender eventSender;
    private final ReservationRepository repository;

    @Autowired
    public ReservationRequestSaga(RabbitMqMessageSender eventSender, ReservationRepository repository) {
        this.eventSender = eventSender;
        this.repository = repository;
    }

    public void executeSaga(Reservation reservation) {
        reservation.validatingUser();
        eventSender.sendValidateUserCommand(reservation.getId(), reservation.getUserEmail());
    }

    public void executeSaga(Long id, UserValidatedEvent event) {
        Reservation reservation = getReservationById(id);
        if (event.getIsClient()) {
            reservation.userValid(event.getUserId());
            eventSender.sendValidateCarCommand(reservation.getId(), reservation.getCarId());
        } else {
            reservation.userInvalid();
            eventSender.sendEmail(reservation.getUserEmail(), generateMessage(reservation.getId(), "You cannot reserve a car, you are not registered in our platform ."));

        }
    }

    public void executeSaga(Long id, CarExistedEvent event) {
        Reservation reservation = getReservationById(id);
        if (event.getIsListed()) {
                reservation.carValid(event.getOwnerEmail(), event.getCarPrice());

                List<Reservation> reservations = repository.findOverlappingReservation( event.getCarId(), reservation.getStartDate(), reservation.getEndDate());

                if (reservations.isEmpty()) {
                    reservation.finalizeReservation();
                    eventSender.sendCreateReservationCommand(reservation);
                    eventSender.sendEmail(reservation.getOwnerEmail(), generateMessage(reservation.getId(), "Proposal for reservation registered. Please accept or decline."));
                }else {
                    reservation.doubleBooking();
                    eventSender.sendEmail(reservation.getUserEmail(), generateMessage(reservation.getId(), "You cannot reserve a car, there was an error in our system."));
                }
        } else {
            reservation.carInvalid();
            eventSender.sendEmail(reservation.getUserEmail(), generateMessage(reservation.getId(), "You cannot reserve a car, the car is not registered in our platform ."));

        }
    }





    public void accept(Long id) {
        Reservation reservation = getReservationById(id);

        if (reservation.getStatus() == ReservationStatus.REQUEST_REGISTERED) {
            reservation.accept();
            long reservationPrice = reservation.getReservationPrice();
            eventSender.sendEmail(reservation.getOwnerEmail(), generateMessage(reservation.getId(), "You car is reserved."));
            eventSender.sendEmail(reservation.getUserEmail(), generateMessage(reservation.getId(), "The owner has accepted your reservation, the total price is " + reservationPrice));

        } else {
            throw new ReservationException("accept","ReservationRequest is in {"+reservation.getStatus() +"} status and is not in a valid status to be accepted");
        }
    }

    public void decline(Long id) {
        Reservation reservation = getReservationById(id);

        if (reservation.getStatus() == ReservationStatus.REQUEST_REGISTERED) {
            reservation.decline();
            eventSender.sendReservationDeclinedEvent(reservation);
            eventSender.sendEmail(reservation.getOwnerEmail(), generateMessage(reservation.getId(), "You decline the reservation."));
            eventSender.sendEmail(reservation.getUserEmail(), generateMessage(reservation.getId(), "The owner has declined your reservation."));

        } else {
            throw new ReservationException("decline","ReservationRequest is in {"+reservation.getStatus() + "} status and is not in a valid status to be declined");
        }
    }

    private Reservation getReservationById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    private String generateMessage(Long id, String message) {
        return "Regarding reservation " + id + ". " + message;
    }
}
