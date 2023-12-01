package be.ucll.project.carservice.web;

import be.ucll.project.carservice.api.CarApiDelegate;
import be.ucll.project.carservice.api.model.ApiCar;
import be.ucll.project.carservice.api.model.ApiCars;
import be.ucll.project.carservice.domain.car.Car;
import be.ucll.project.carservice.domain.car.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CarController implements CarApiDelegate {


    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Override
    public ResponseEntity<ApiCars> getCars() {
        ApiCars cars = new ApiCars();
        cars.setCars(
                carService.getCars().stream()
                        .map(this::toDto)
                        .toList()
        );

        return ResponseEntity.ok(cars);
    }

    @Override
    public ResponseEntity<ApiCar> createCar(ApiCar data) {
        return ResponseEntity.ok(toDto(carService.createCar(data)));
    }

    @Override
    public ResponseEntity<ApiCars> searchCars(String model,
                                             String location,
                                             Integer minPrice,
                                             Integer maxPrice,
                                             LocalDate startDate,
                                             LocalDate endDate) {

        ApiCars cars = new ApiCars();
        cars.setCars(
                carService.searchCar(model, location, minPrice, maxPrice, startDate, endDate).stream()
                        .map(this::toDto)
                        .toList()
        );

        return ResponseEntity.ok(cars);

    }

    @Override
    public ResponseEntity<ApiCar> removeCar(Long carId) {
        return ResponseEntity.ok(toDto(carService.removeCar(carId)));
    }

    private ApiCar toDto(Car car) {
        return new ApiCar()
                .id(car.getId())
                .model(car.getModel())
                .location(car.getLocation())
                .price(car.getPrice())
                .owner(car.getOwner());
    }
}
