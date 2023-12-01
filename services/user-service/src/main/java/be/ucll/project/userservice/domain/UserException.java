package be.ucll.project.userservice.domain;


public class UserException extends RuntimeException{

    private final String action;

    public String getAction() {
        return action;
    }


    public UserException(String action, String message) {
        super(message);
        this.action = action;
    }

}
