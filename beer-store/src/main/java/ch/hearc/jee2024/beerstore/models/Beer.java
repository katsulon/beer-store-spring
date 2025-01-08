package ch.hearc.jee2024.beerstore.models;

import jakarta.persistence.*;

import javax.annotation.processing.Generated;

@Entity
@Table(name = "beers")
public class Beer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String manufacturer;
    private String name;
    private String description;
    private double alcohol;
    private double price;

    public Beer() { }

    public Beer(String manufacturer, String name, String description, double alcohol, double price) {
        this.manufacturer = manufacturer;
        this.name = name;
        this.description = description;
        this.alcohol = alcohol;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getManufacturer() {
        return manufacturer;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
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

    @Override
    public String toString() {
        return "Beer{" +
                "id=" + id +
                ", manufacturer='" + manufacturer + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", alcohol=" + alcohol +
                ", price=" + price +
                '}';
    }
}
