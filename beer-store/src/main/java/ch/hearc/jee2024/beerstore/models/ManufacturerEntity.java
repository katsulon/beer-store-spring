package ch.hearc.jee2024.beerstore.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "manufacturers")
public class ManufacturerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;
    @OneToMany(mappedBy = "manufacturer")
    @JsonIgnore
    List<BeerEntity> beers = new ArrayList<>();

    public ManufacturerEntity() { }

    public ManufacturerEntity(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public List<BeerEntity> getBeers() {
        return beers;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setBeers(List<BeerEntity> beers) {
        this.beers = beers;
    }

    @Override
    public String toString() {
        return "ManufacturerEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

}