package ch.hearc.jee2024.beerstore;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Beer Shop API", version = "1.0", description = "API for managing beers"))
@SpringBootApplication
public class BeerStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(BeerStoreApplication.class, args);
    }
}

