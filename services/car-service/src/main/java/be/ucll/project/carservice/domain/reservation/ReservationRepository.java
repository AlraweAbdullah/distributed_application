package be.ucll.project.carservice.domain.reservation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

        @Query("SELECT r FROM Reservation r " +
                "WHERE r.carId = :carId " +
                "AND (r.startDate BETWEEN :startDate AND :endDate " +
                "OR r.endDate BETWEEN :startDate AND :endDate " +
                "OR (r.startDate <= :startDate AND r.endDate >= :endDate))")
        List<Reservation> findOverlappingReservation(
                @Param("carId") Long carId,
                @Param("startDate") LocalDate startDate,
                @Param("endDate") LocalDate endDate);


        List<Reservation> findByCarId(long carId);

}
