package be.ucll.project.carservice.domain.car;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends CrudRepository<Car, Long> {
    List<Car> findByModelAndLocationAndPriceBetween(String model, String location, int minPrice, int maxPrice);

}
