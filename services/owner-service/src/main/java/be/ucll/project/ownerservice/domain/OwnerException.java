package be.ucll.project.ownerservice.domain;


public class OwnerException extends RuntimeException{

    private final String action;

    public String getAction() {
        return action;
    }


    public OwnerException(String action, String message) {
        super(message);
        this.action = action;
    }

}
