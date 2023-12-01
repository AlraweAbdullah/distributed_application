package be.ucll.project.reservationservice.domain.reservation;

public class ReservationException extends RuntimeException{
    private final String action;

    public String getAction() {
        return action;
    }


    public ReservationException(String action, String message) {
        super(message);
        this.action = action;
    }
}
