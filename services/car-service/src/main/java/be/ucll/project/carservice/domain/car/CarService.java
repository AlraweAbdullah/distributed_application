package be.ucll.project.carservice.domain.car;

import be.ucll.project.carservice.api.model.ApiCar;
import be.ucll.project.carservice.domain.owner.OwnerRepository;
import be.ucll.project.carservice.domain.reservation.Reservation;
import be.ucll.project.carservice.domain.reservation.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CarService {

    private final CarRepository carRepository;
    private final OwnerRepository ownerRepository;

    private final ReservationRepository reservationRepository;


    @Autowired
    public CarService(CarRepository carRepository, OwnerRepository ownerRepository, ReservationRepository reservationRepository) {
        this.carRepository = carRepository;
        this.ownerRepository = ownerRepository;
        this.reservationRepository = reservationRepository;
    }


    public Car createCar(ApiCar data) {
        if (ownerRepository.findById(data.getOwner()).isEmpty()) {
            throw new CarException("add", "owner not found");
        }

        return carRepository.save(new Car(data.getModel(), data.getLocation(), data.getPrice(), data.getOwner()));
    }

    public List<Car> getCars() {
        List<Car> carsList = new ArrayList<>();

        carRepository.findAll().forEach(carsList::add);
        return carsList;
    }

    public List<Car> searchCar(String model,
                               String location,
                               Integer minPrice,
                               Integer maxPrice,
                               LocalDate startDate,
                               LocalDate endDate) {
        {
            List<Car> carsList = carRepository.findByModelAndLocationAndPriceBetween(model, location, minPrice, maxPrice);
            List<Car> cars = new ArrayList<>();


            for (Car car : carsList) {
                List<Reservation> reservation = reservationRepository.findOverlappingReservation(car.getId(), startDate, endDate);

                if (reservation.isEmpty()) {
                    cars.add(car);
                }
            }
            return cars;

        }
    }

    public Car removeCar(Long carId) {
        Car car = carRepository.findById(carId).orElse(null);
        if (car == null) {
            throw new CarException("remove", "car not found");
        }

        if (!reservationRepository.findByCarId(car.getId()).isEmpty()){
            throw new CarException("remove", "car is reserved");

        }
        carRepository.delete(car);

        return car;
    }

    public Car validateCar(Long carId) {
        return carRepository.findById(carId).orElse(null);
    }
}
