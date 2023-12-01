package be.ucll.project.reservationservice.domain.reservation;


import be.ucll.project.reservationservice.api.model.ApiReservationConfirmation;
import be.ucll.project.reservationservice.api.model.ApiReservationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository repository;
    private final ReservationRequestSaga requestSaga;

    @Autowired
    public ReservationService(ReservationRepository repository, ReservationRequestSaga requestSaga) {
        this.repository = repository;
        this.requestSaga = requestSaga;
    }

    public String requestReservation(ApiReservationRequest request) {
        Reservation reservation = new Reservation(request.getUserEmail(), request.getCarId(), request.getStartDate(), request.getEndDate());

        reservation = repository.save(reservation);
        requestSaga.executeSaga(reservation);

        return reservation.getId().toString();
    }

    public void finalizeReservation(ApiReservationConfirmation apiReservationConfirmation) {
        if (apiReservationConfirmation.getAcceptProposedReservation()) {
            requestSaga.accept(Long.valueOf(apiReservationConfirmation.getReservationRequestNumber()));
        } else {
            requestSaga.decline(Long.valueOf(apiReservationConfirmation.getReservationRequestNumber()));
        }
    }
}
