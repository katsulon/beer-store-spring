package ch.hearc.jee2024.beerstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BeerStoreApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void newControllerExist() throws Exception {
        String controllerExpectedFqdn = "ch.hearc.jee2024.beerstore.controllers.BeerController";

        try {
            Class<?> controller = Class.forName(controllerExpectedFqdn);
            boolean isAnnotation = controller.isAnnotationPresent(RestController.class);
            assertTrue(isAnnotation, String.format("Controller %s is not a RestController", controllerExpectedFqdn));
        } catch (ClassNotFoundException ex) {
            fail(String.format("Controller %s doesn't exist", controllerExpectedFqdn));
        }
    }

    @Test
    public void callBeerShouldCreateBeer() throws Exception {
        this.mvc.perform(post("/beer")
                        .contentType("application/json")
                        .content("{\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}"));
    }

    @Test
    public void callBeersShouldReturnBeers() throws Exception {
        this.mvc.perform(post("/beer")
                        .contentType("application/json")
                        .content("{\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}"))
                .andExpect(status().isCreated());

        this.mvc.perform(post("/beer")
                        .contentType("application/json")
                        .content("{\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer 2\",\"description\":\"A test beer description.\",\"alcohol\":6.0,\"price\":3.0}"))
                .andExpect(status().isCreated());

        this.mvc.perform(get("/beer"))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}," +
                        "{\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer 2\",\"description\":\"A test beer description.\",\"alcohol\":6.0,\"price\":3.0}" +
                        "]"));
    }

    @Test
    public void callBeerWithIdShouldReturnBeer() throws Exception {
        this.mvc.perform(post("/beer")
                        .contentType("application/json")
                        .content("{\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}"))
                .andExpect(status().isCreated());

        this.mvc.perform(get("/beer/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}"));
    }

    @Test
    public void callBeerWithIdShouldReturnNotFound() throws Exception {
        this.mvc.perform(get("/beer/4"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void callBeerWithIdShouldUpdateBeer() throws Exception {
        this.mvc.perform(post("/beer")
                        .contentType("application/json")
                        .content("{\"manufacturer\":\"Initial Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}"))
                .andExpect(status().isCreated());

        this.mvc.perform(put("/beer/1")
                        .contentType("application/json")
                        .content("{\"manufacturer\":\"Test Brewery\",\"name\":\"Updated Test Beer\",\"description\":\"Updated description.\",\"alcohol\":6.0,\"price\":3.5}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"manufacturer\":\"Test Brewery\",\"name\":\"Updated Test Beer\",\"description\":\"Updated description.\",\"alcohol\":6.0,\"price\":3.5}"));
    }

    @Test
    public void callBeerWithIdShouldReturnNotFoundOnUpdate() throws Exception {
        this.mvc.perform(put("/beer/3")
                        .contentType("application/json")
                        .content("{\"manufacturer\":\"Test Brewery\",\"name\":\"Non-existent Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":3.5}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void callBeerWithIdShouldDeleteBeer() throws Exception {
        this.mvc.perform(post("/beer")
                        .contentType("application/json")
                        .content("{\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}"))
                .andExpect(status().isCreated());

        this.mvc.perform(delete("/beer/1"))
                .andExpect(status().isNoContent());

        this.mvc.perform(get("/beer/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void callBeerWithIdShouldReturnNotFoundOnDelete() throws Exception {
        this.mvc.perform(delete("/beer/3"))
                .andExpect(status().isNotFound());
    }
}
