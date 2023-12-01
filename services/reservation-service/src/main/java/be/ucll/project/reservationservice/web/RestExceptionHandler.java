package be.ucll.project.reservationservice.web;

import be.ucll.project.reservationservice.api.model.ApiError;
import be.ucll.project.reservationservice.domain.reservation.ReservationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ReservationException.class})
    public ResponseEntity<ApiError> handleServiceException(Exception ex) {
        ApiError error = new ApiError();
        error.setCode(((ReservationException)ex).getAction());
        error.setMessage(ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }
}
