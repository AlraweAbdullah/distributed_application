package be.ucll.project.ownerservice.domain;


public class ServiceException extends RuntimeException{

    private final String action;

    public String getAction() {
        return action;
    }


    public ServiceException(String action, String message) {
        super(message);
        this.action = action;
    }

}
