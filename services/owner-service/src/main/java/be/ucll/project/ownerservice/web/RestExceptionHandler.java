package be.ucll.project.ownerservice.web;

import be.ucll.project.ownerservice.api.model.ApiError;
import be.ucll.project.ownerservice.domain.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<ApiError> handleServiceException(Exception ex) {
        ApiError error = new ApiError();
        error.setCode(((ServiceException)ex).getAction());
        error.setMessage(ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }
}
