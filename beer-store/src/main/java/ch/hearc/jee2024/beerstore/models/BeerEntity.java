package ch.hearc.jee2024.beerstore.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@Entity
@Table(name = "beers")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BeerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private ManufacturerEntity manufacturer;
    private String name;
    private String description;
    private double alcohol;
    private double price;

    public BeerEntity() { }

    public BeerEntity(ManufacturerEntity manufacturer, String name, String description, double alcohol, double price) {
        this.manufacturer = manufacturer;
        this.name = name;
        this.description = description;
        this.alcohol = alcohol;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getAlcohol() {
        return alcohol;
    }

    public double getPrice() {
        return price;
    }

    public ManufacturerEntity getManufacturer() {
        return manufacturer;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAlcohol(double alcohol) {
        this.alcohol = alcohol;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setManufacturer(ManufacturerEntity manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Beer{" +
                "id=" + id +
                ", manufacturer='" + (manufacturer != null ? manufacturer.getName() : "null") + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", alcohol=" + alcohol +
                ", price=" + price +
                '}';
    }
}
