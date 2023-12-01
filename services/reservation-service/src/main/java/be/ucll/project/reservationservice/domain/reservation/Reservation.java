package be.ucll.project.reservationservice.domain.reservation;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private Integer carPrice;
    private Boolean isValidUserId;

    private Boolean isValidCarId;

    private Long reservationPrice;

    private String userEmail;

    private String ownerEmail;

    public LocalDate getStartDate() {
        return startDate;
    }


    public LocalDate getEndDate() {
        return endDate;
    }


    private LocalDate startDate;
    private LocalDate endDate;

    private Long carId;


    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    protected Reservation() {}

    public Reservation(String userEmail, Long carId, LocalDate startDate, LocalDate endDate) {
        this.userEmail = userEmail;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = ReservationStatus.REGISTERED;
    }

    public Long getId() {
        return id;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void validatingUser() {
        this.status = ReservationStatus.VALIDATING_USER;
    }

    public void userValid(Long userId) {
        this.status = ReservationStatus.VALIDATING_CAR;
        this.isValidUserId = true;
        this.userId = userId;
    }

    public void userInvalid() {
        this.status = ReservationStatus.NO_USER;
        this.isValidUserId = false;
    }


    public void carValid(String ownerEmail, Integer carPrice) {
        this.status = ReservationStatus.VALIDATING_RESERVATION;
        this.isValidCarId = true;
        this.ownerEmail = ownerEmail;
        this.carPrice = carPrice;
    }

    public void carInvalid() {
        this.status = ReservationStatus.NO_CAR;
        this.isValidCarId = false;
    }

    public void doubleBooking() {
        this.status = ReservationStatus.DOUBLE_BOOKING;
    }

    public void accept() {
        this.status = ReservationStatus.ACCEPTED;
        long differenceInDays = Period.between(startDate, endDate).getDays() + 1;
        this.reservationPrice = differenceInDays * carPrice;

    }

    public void decline() {
        this.status = ReservationStatus.DECLINED;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCarId() {
        return carId;
    }
    public void finalizeReservation() {
        this.status = ReservationStatus.REQUEST_REGISTERED;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getOwnerEmail() {
        return ownerEmail;

    }


    public Long getReservationPrice() {
        return reservationPrice;
    }
}