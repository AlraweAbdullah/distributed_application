package be.ucll.project.carservice.messaging;

import be.ucll.project.carservice.api.model.CarExistedEvent;
import be.ucll.project.carservice.api.model.CheckCarExistCommand;
import be.ucll.project.carservice.api.model.CreateReservationCommand;
import be.ucll.project.carservice.api.model.DeclineReservationCommand;
import be.ucll.project.carservice.client.owner.api.model.OwnerCreatedEvent;
import be.ucll.project.carservice.domain.car.Car;
import be.ucll.project.carservice.domain.car.CarService;
import be.ucll.project.carservice.domain.owner.Owner;
import be.ucll.project.carservice.domain.owner.OwnerRepository;
import be.ucll.project.carservice.domain.reservation.Reservation;
import be.ucll.project.carservice.domain.reservation.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MessageListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);
    private final RabbitTemplate rabbitTemplate;

    private final OwnerRepository ownerRepository;

    private final ReservationRepository reservationRepository;

    private final CarService carService;

    @Autowired
    public MessageListener(OwnerRepository ownerRepository, ReservationRepository reservationRepository, CarService carService, RabbitTemplate rabbitTemplate) {
        this.reservationRepository = reservationRepository;
        this.ownerRepository = ownerRepository;
        this.carService = carService;
        this.rabbitTemplate = rabbitTemplate;

    }

    @RabbitListener(queues = {"q.owner-car-service-owner-created"})
    public void onOwnerCreated(OwnerCreatedEvent event) {
        LOGGER.info("Received command: " + event);
        Owner owner = new Owner(event.getOwner().getId(), event.getOwner().getEmail());
        this.ownerRepository.save(owner);
    }

    @RabbitListener(queues = {"q.car-service.check-car-exist"})
    public void onExistedCar(CheckCarExistCommand command) {
        LOGGER.info("Received command: " + command);

        Car car = carService.validateCar(command.getCarId());

        CarExistedEvent event = new CarExistedEvent();

        event.setReservationId(command.getReservationId());
        event.setCarId(command.getCarId());
        event.setIsListed(false);

        if (car != null) {
            Owner owner = ownerRepository.findById(car.getOwner()).orElse(null);
            if (owner != null) {
                event.setIsListed(true);
                event.setCarPrice(car.getPrice());
                event.setOwnerEmail(owner.getEmail());
            }
        }
        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.car-exist", "", event);
    }


    @RabbitListener(queues = {"q.car-service.reservation-created"})
    public void onReservationCreated(CreateReservationCommand event) {

        LOGGER.info("Received command: " + event);
        Reservation reservation = new Reservation(event.getReservationId(), event.getCarId(), event.getStartDate(), event.getEndDate());
        this.reservationRepository.save(reservation);
    }


    @RabbitListener(queues = {"q.car-service.reservation-declined"})
    public void onReservationDeclined(DeclineReservationCommand event) {

        LOGGER.info("Received command: " + event);
        Reservation reservation = this.reservationRepository.findById(event.getReservationId()).orElse(null);
        this.reservationRepository.delete(reservation);

    }

}
