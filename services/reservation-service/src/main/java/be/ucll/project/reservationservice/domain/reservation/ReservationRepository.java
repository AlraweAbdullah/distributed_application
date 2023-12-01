package be.ucll.project.reservationservice.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.carId = :carId " +
            "AND (r.status = be.ucll.project.reservationservice.domain.reservation.ReservationStatus.ACCEPTED OR " +
            "r.status = be.ucll.project.reservationservice.domain.reservation.ReservationStatus.REQUEST_REGISTERED ) " +
            "AND (r.startDate BETWEEN :startDate AND :endDate " +
            "OR r.endDate BETWEEN :startDate AND :endDate " +
            "OR (r.startDate <= :startDate AND r.endDate >= :endDate))")
    List<Reservation> findOverlappingReservation(
            @Param("carId") Long carId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}
