package be.ucll.project.reservationservice.domain.reservation;

public enum ReservationStatus {

    // Happy Flow
    REGISTERED,
    VALIDATING_USER,
    VALIDATING_CAR,

    REQUEST_REGISTERED,
    VALIDATING_RESERVATION,

    // Failure States
    NO_USER,
    NO_CAR,
    DOUBLE_BOOKING,

    // End States
    ACCEPTED,
    DECLINED;
}
