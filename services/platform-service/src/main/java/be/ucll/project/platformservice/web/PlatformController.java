package be.ucll.project.platformservice.web;


import be.ucll.project.platformservice.api.PlatformApiDelegate;
import be.ucll.project.platformservice.api.model.*;
import be.ucll.project.platformservice.client.car.api.CarApi;
import be.ucll.project.platformservice.client.owner.api.OwnerApi;
import be.ucll.project.platformservice.client.reservation.api.ReservationApi;
import be.ucll.project.platformservice.client.user.api.UserApi;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class PlatformController implements PlatformApiDelegate {

    @Autowired
    private OwnerApi ownerApi;

    @Autowired
    private UserApi userApi;


    @Autowired
    private CarApi carApi;

    @Autowired
    private ReservationApi reservationApi;

    @Autowired
    private EurekaClient discoveryClient;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public ResponseEntity<ApiOwners> getOwners() {
        ApiOwners overview = new ApiOwners();

        ownerApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("owner-service", false).getHomePageUrl());
        be.ucll.project.platformservice.client.owner.model.ApiOwners owners = circuitBreakerFactory.create("ownerApi").run(() -> ownerApi.getOwners());

        if (owners.getOwners() == null || owners.getOwners().isEmpty()) {
            return ResponseEntity.ok(overview);
        }
        for (be.ucll.project.platformservice.client.owner.model.ApiOwner owner : owners.getOwners()) {

            overview.addOwnersItem(toResponse(owner));
        }

        return ResponseEntity.ok(overview);

    }

    @Override
    public ResponseEntity<ApiOwner> createOwner(ApiOwner apiOwner) {

        be.ucll.project.platformservice.client.owner.model.ApiOwner client = new be.ucll.project.platformservice.client.owner.model.ApiOwner();
        client.setEmail(apiOwner.getEmail());
        client.setName(apiOwner.getName());

        ownerApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("owner-service", false).getHomePageUrl());
        be.ucll.project.platformservice.client.owner.model.ApiOwner owner = circuitBreakerFactory.create("ownerApi").run(() -> ownerApi.createOwner(client));


        return ResponseEntity.ok(toResponse(owner));

    }


    @Override
    public ResponseEntity<ApiUsers> getUsers() {
        ApiUsers overview = new ApiUsers();

        userApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("user-service", false).getHomePageUrl());
        be.ucll.project.platformservice.client.user.model.ApiUsers users = circuitBreakerFactory.create("userApi").run(() -> userApi.getUsers());

        if (users.getUsers() == null || users.getUsers().isEmpty()) {
            return ResponseEntity.ok(overview);
        }
        for (be.ucll.project.platformservice.client.user.model.ApiUser user : users.getUsers()) {

            overview.addUsersItem(toResponse(user));
        }

        return ResponseEntity.ok(overview);

    }


    @Override
    public ResponseEntity<ApiUser> createUser(ApiUser apiUser) {

        be.ucll.project.platformservice.client.user.model.ApiUser client = new be.ucll.project.platformservice.client.user.model.ApiUser();
        client.setEmail(apiUser.getEmail());
        client.setName(apiUser.getName());

        userApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("user-service", false).getHomePageUrl());
        be.ucll.project.platformservice.client.user.model.ApiUser user = circuitBreakerFactory.create("userApi").run(() -> userApi.createUser(client));


        return ResponseEntity.ok(toResponse(user));

    }


    @Override
    public ResponseEntity<ApiCar> createCar(ApiCar apiCar) {

        be.ucll.project.platformservice.client.car.model.ApiCar client = new be.ucll.project.platformservice.client.car.model.ApiCar();
        client.setLocation(apiCar.getLocation());
        client.setModel(apiCar.getModel());
        client.setOwner(apiCar.getOwner());
        client.setPrice(apiCar.getPrice());

        carApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("car-service", false).getHomePageUrl());
        be.ucll.project.platformservice.client.car.model.ApiCar car = circuitBreakerFactory.create("carApi").run(() -> carApi.createCar(client));


        return ResponseEntity.ok(toResponse(car));

    }


    @Override
    public ResponseEntity<ApiCars> getCars() {
        ApiCars overview = new ApiCars();

        carApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("car-service", false).getHomePageUrl());
        be.ucll.project.platformservice.client.car.model.ApiCars cars = circuitBreakerFactory.create("carrApi").run(() -> carApi.getCars());

        if (cars.getCars() == null || cars.getCars().isEmpty()) {
            return ResponseEntity.ok(overview);
        }

        for (be.ucll.project.platformservice.client.car.model.ApiCar car : cars.getCars()) {

            overview.addCarsItem(toResponse(car));
        }

        return ResponseEntity.ok(overview);

    }

    @Override
    public ResponseEntity<ApiCar> removeCar(Long carId) {
        carApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("car-service", false).getHomePageUrl());
        be.ucll.project.platformservice.client.car.model.ApiCar car = circuitBreakerFactory.create("carrApi").run(() -> carApi.removeCar(carId));

        return ResponseEntity.ok(toResponse(car));

    }

    @Override
    public ResponseEntity<ApiCars> searchCars(String model, String location, Integer minPrice, Integer maxPrice, LocalDate startDate, LocalDate endDate) {
        ApiCars overview = new ApiCars();

        carApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("car-service", false).getHomePageUrl());
        be.ucll.project.platformservice.client.car.model.ApiCars cars = circuitBreakerFactory.create("carrApi").run(() -> carApi.searchCars(model, location, minPrice, maxPrice, startDate, endDate));

        if (cars.getCars() == null || cars.getCars().isEmpty()) {
            return ResponseEntity.ok(overview);
        }
        for (be.ucll.project.platformservice.client.car.model.ApiCar car : cars.getCars()) {
            overview.addCarsItem(toResponse(car));
        }
        return PlatformApiDelegate.super.searchCars(model, location, minPrice, maxPrice, startDate, endDate);
    }


    @Override
    public ResponseEntity<ApiReservationRequestResponse> requestReservation(ApiReservationRequest apiReservationRequest) {
        be.ucll.project.platformservice.client.reservation.model.ApiReservationRequest reservationRequest = new be.ucll.project.platformservice.client.reservation.model.ApiReservationRequest();
        reservationRequest.setCarId(apiReservationRequest.getCarId());
        reservationRequest.setUserEmail(apiReservationRequest.getUserEmail());
        reservationRequest.setStartDate(apiReservationRequest.getStartDate());
        reservationRequest.setEndDate(apiReservationRequest.getEndDate());

        reservationApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("reservation-service", false).getHomePageUrl());
        be.ucll.project.platformservice.client.reservation.model.ApiReservationRequestResponse client = circuitBreakerFactory.create("reservationApi").run(() -> reservationApi.requestReservation(reservationRequest));

        ApiReservationRequestResponse response = new ApiReservationRequestResponse();
        response.setReservationRequestNumber(client.getReservationRequestNumber());
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<Void> finalizeReservation(ApiReservationConfirmation apiReservationConfirmation) {
        be.ucll.project.platformservice.client.reservation.model.ApiReservationConfirmation reservationConfirmation = new be.ucll.project.platformservice.client.reservation.model.ApiReservationConfirmation();
        reservationConfirmation.setReservationRequestNumber(apiReservationConfirmation.getReservationRequestNumber());
        reservationConfirmation.setAcceptProposedReservation(apiReservationConfirmation.getAcceptProposedReservation());


        reservationApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("reservation-service", false).getHomePageUrl());
        be.ucll.project.platformservice.client.reservation.model.ApiReservationRequestResponse client = circuitBreakerFactory.create("reservationApi").run(() ->
        {
            reservationApi.finalizeReservation(reservationConfirmation);
            return null;
        });

        return ResponseEntity.ok().build();
    }

    private ApiCar toResponse(be.ucll.project.platformservice.client.car.model.ApiCar car) {
        ApiCar response = new ApiCar();
        response.setId(car.getId());
        response.setLocation(car.getLocation());
        response.setModel(car.getModel());
        response.setOwner(car.getOwner());
        response.setPrice(car.getPrice());

        return response;
    }


    private ApiUser toResponse(be.ucll.project.platformservice.client.user.model.ApiUser user) {
        ApiUser response = new ApiUser();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());

        return response;
    }

    private ApiOwner toResponse(be.ucll.project.platformservice.client.owner.model.ApiOwner owner) {
        ApiOwner response = new ApiOwner();
        response.setId(owner.getId());
        response.setEmail(owner.getEmail());
        response.setName(owner.getName());

        return response;
    }
}
