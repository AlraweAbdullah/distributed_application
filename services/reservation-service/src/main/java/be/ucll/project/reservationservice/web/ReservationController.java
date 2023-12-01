package be.ucll.project.reservationservice.web;

import be.ucll.project.reservationservice.api.ReservationApiDelegate;
import be.ucll.project.reservationservice.api.model.ApiReservationConfirmation;
import be.ucll.project.reservationservice.api.model.ApiReservationRequest;
import be.ucll.project.reservationservice.api.model.ApiReservationRequestResponse;
import be.ucll.project.reservationservice.domain.reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationController implements ReservationApiDelegate {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public ResponseEntity<ApiReservationRequestResponse> requestReservation(ApiReservationRequest apiReservationRequest) {
        ApiReservationRequestResponse response = new ApiReservationRequestResponse();
        response.reservationRequestNumber(reservationService.requestReservation(apiReservationRequest));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> finalizeReservation(ApiReservationConfirmation apireservationConfirmation) {
        reservationService.finalizeReservation(apireservationConfirmation);
        return ResponseEntity.ok().build();
    }
}
