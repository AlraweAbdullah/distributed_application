package be.ucll.project.carservice.domain.reservation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Reservation {
    @Id
    private Long id;
    private Long carId;

    private LocalDate startDate;

    private LocalDate endDate;



    public Reservation(Long id, Long car, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.carId = car;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    protected Reservation() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
