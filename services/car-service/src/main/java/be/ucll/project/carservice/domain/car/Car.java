package be.ucll.project.carservice.domain.car;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity
public class Car {
    @Id
    @GeneratedValue
    private Long id;
    private String model;
    private String location;
    private int price;
    private Long owner;


    protected Car() {}

    public Car(String model, String location, int price, Long owner) {
        this.model = model;
        this.location = location;
        this.price = price;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", location='" + location + '\'' +
                ", price=" + price +
                ", owner=" + owner +
                '}';
    }
}
