package be.ucll.project.carservice.web;

import be.ucll.project.carservice.api.model.ApiError;
import be.ucll.project.carservice.domain.car.CarException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({CarException.class})
    public ResponseEntity<ApiError> handleServiceException(Exception ex) {
        ApiError error = new ApiError();
        error.setCode(((CarException)ex).getAction());
        error.setMessage(ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }
}
