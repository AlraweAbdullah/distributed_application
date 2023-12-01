package be.ucll.project.userservice.web;

import be.ucll.project.userservice.api.model.ApiError;
import be.ucll.project.userservice.domain.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserException.class})
    public ResponseEntity<ApiError> handleServiceException(Exception ex) {
        ApiError error = new ApiError();
        error.setCode(((UserException)ex).getAction());
        error.setMessage(ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }
}
