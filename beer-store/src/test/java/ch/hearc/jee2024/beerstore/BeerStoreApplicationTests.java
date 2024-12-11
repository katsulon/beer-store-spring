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
@AutoConfigureMockMvc // automatic configuration of the mock
class BeerStoreTests {

    @Autowired
    private MockMvc mvc; // object allowing to "simulate" a web server
    @Test
    public void newControllerExist() throws Exception {
        String controllerExpectedFqdn = "ch.hearc.jee2024.beerstore.controllers.BeerController";

        try{
            Class controller = Class.forName(controllerExpectedFqdn);
            boolean isAnnotation = controller.isAnnotationPresent(RestController.class);
            assertTrue(isAnnotation,String.format("Controller %s is not a RestController",controllerExpectedFqdn));

        }catch(ClassNotFoundException ex){
            fail(String.format("Controller %s doesn't exist",controllerExpectedFqdn));
        }
    }

    @Test
    public void callBeerShouldCreateBeer() throws Exception {
        this.mvc.perform(post("/beer") //POST /beer
                        .contentType("application/json") //Type de contenu
                        .content("{\"id\":\"1\",\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}")) //Contenu de la requête
                .andExpect(status().isCreated()) //Status code = 200
                .andExpect(content().json("{\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}")); //Réponse attendue
    }

    @Test
    public void callBeersShouldReturnBeers() throws Exception {
        this.mvc.perform(post("/beer") //POST /beer
                        .contentType("application/json") //Type de contenu
                        .content("{\"id\":\"1\",\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}")) //Contenu de la requête
                .andExpect(status().isCreated()) //Status code = 200
                .andExpect(content().json("{\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}")); //Réponse attendue

        this.mvc.perform(post("/beer") //POST /beer
                        .contentType("application/json") //Type de contenu
                        .content("{\"id\":\"2\",\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer 2\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}")) //Contenu de la requête
                .andExpect(status().isCreated()) //Status code = 200
                .andExpect(content().json("{\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer 2\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}")); //Réponse attendue

        this.mvc.perform(get("/beer")) //GET /beer
                .andExpect(status().isOk()) //Status code = 200
                .andExpect(content().json("[" +
                        "{" +
                        "\"id\":1," +
                        "\"manufacturer\":\"Test Brewery\"," +
                        "\"name\":\"Test Beer\"," +
                        "\"description\":\"A test beer description.\"," +
                        "\"alcohol\":5.0," +
                        "\"price\":2.5" +
                        "}," +
                        "{" +
                        "\"id\":2," +
                        "\"manufacturer\":\"Test Brewery\"," +
                        "\"name\":\"Test Beer 2\"," +
                        "\"description\":\"A test beer description.\"," +
                        "\"alcohol\":5.0," +
                        "\"price\":2.5" +
                        "}" +
                        "]"));
    }

    @Test
    public void callBeerWithIdShouldReturnBeer() throws Exception {
        this.mvc.perform(post("/beer") //POST /beer
                        .contentType("application/json") //Type de contenu
                        .content("{\"id\":\"1\",\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}")) //Contenu de la requête
                .andExpect(status().isCreated()) //Status code = 200
                .andExpect(content().json("{\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}")); //Réponse attendue

        this.mvc.perform(get("/beer/1")) //GET /beer/1
                .andExpect(status().isOk()) //Status code = 200
                .andExpect(content().json("{\"id\":1,\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}")); //Réponse attendue
    }

    @Test
    public void callBeerWithIdShouldReturnNotFound() throws Exception {
        this.mvc.perform(get("/beer/3")) //GET /beer/1
                .andExpect(status().isNotFound()); //Status code = 404
    }

    @Test
    public void callBeerWithIdShouldUpdateBeer() throws Exception {
        this.mvc.perform(post("/beer") //POST /beer
                        .contentType("application/json") //Type de contenu
                        .content("{\"id\":\"1\",\"manufacturer\":\"Initial Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}")) //Contenu de la requête
                .andExpect(status().isCreated()) //Status code = 200
                .andExpect(content().json("{\"manufacturer\":\"Initial Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}")); //Réponse attendue

        this.mvc.perform(put("/beer/1") //PUT /beer/1
                        .contentType("application/json") //Type de contenu
                        .content("{\"id\":\"1\",\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":3.5}")) //Contenu de la requête
                .andExpect(status().isOk()) //Status code = 200
                .andExpect(content().json("{\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":3.5}")); //Réponse attendue
    }

    @Test
    public void callBeerWithIdShouldReturnNotFoundOnUpdate() throws Exception {
        this.mvc.perform(put("/beer/3") //PUT /beer/3
                        .contentType("application/json") //Type de contenu
                        .content("{\"id\":\"3\",\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":3.5}")) //Contenu de la requête
                .andExpect(status().isNotFound()); //Status code = 404
    }

    @Test
    public void callBeerWithIdShouldDeleteBeer() throws Exception {
        this.mvc.perform(post("/beer") //POST /beer
                        .contentType("application/json") //Type de contenu
                        .content("{\"id\":\"1\",\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}")) //Contenu de la requête
                .andExpect(status().isCreated()) //Status code = 200
                .andExpect(content().json("{\"manufacturer\":\"Test Brewery\",\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5}")); //Réponse attendue

        this.mvc.perform(delete("/beer/1")) //DELETE /beer/1
                .andExpect(status().isNoContent()); //Status code = 204

        this.mvc.perform(get("/beer/1")) //GET /beer/1
                .andExpect(status().isNotFound()); //Status code = 404
    }

    @Test
    public void callBeerWithIdShouldReturnNotFoundOnDelete() throws Exception {
        this.mvc.perform(delete("/beer/3")) //DELETE /beer/3
                .andExpect(status().isNotFound()); //Status code = 404
    }
}
