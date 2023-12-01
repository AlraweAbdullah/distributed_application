package be.ucll.project.carservice.domain.car;

public class CarException extends RuntimeException{
    private final String action;

    public String getAction() {
        return action;
    }


    public CarException(String action, String message) {
        super(message);
        this.action = action;
    }
}
