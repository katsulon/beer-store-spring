package ch.hearc.jee2024.beerstore;

import ch.hearc.jee2024.beerstore.models.BeerEntity;
import ch.hearc.jee2024.beerstore.models.ManufacturerEntity;
import ch.hearc.jee2024.beerstore.models.UserEntity;
import ch.hearc.jee2024.beerstore.models.orders.OrderBeerEntity;
import ch.hearc.jee2024.beerstore.models.orders.OrderEntity;
import ch.hearc.jee2024.beerstore.services.BeerService;
import ch.hearc.jee2024.beerstore.services.ManufacturerService;
import ch.hearc.jee2024.beerstore.services.OrderService;
import ch.hearc.jee2024.beerstore.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ManufacturerService manufacturerService;

    @MockitoBean
    private BeerService beerService;

    @MockitoBean
    private OrderService orderService;

    String admin = Base64.getEncoder().encodeToString("admin:admin".getBytes());

    // Clean up the database after each test to avoid conflicts
    @AfterEach
    public void tearDown() {
        Pageable pageable = PageRequest.of(0, 5);

        // Null-safe checks before iterating
        if (manufacturerService.list(pageable) != null) {
            for (ManufacturerEntity manufacturer : manufacturerService.list(pageable)) {
                manufacturerService.delete(manufacturer.getId());
            }
        }

        if (beerService.list(pageable) != null) {
            for (BeerEntity beer : beerService.list(pageable)) {
                beerService.delete(beer.getId());
            }
        }

        if (orderService.list(pageable) != null) {
            for (OrderEntity order : orderService.list(pageable)) {
                orderService.delete(order.getId());
            }
        }

        if (userService.list(pageable) != null) {
            for (UserEntity user : userService.list(pageable)) {
                userService.delete(user.getId());
            }
        }
    }

    @Test
    public void controllersExist() throws Exception {
        String beerControllerExpectedFqdn = "ch.hearc.jee2024.beerstore.controllers.BeerController";
        String manufacturerControllerExpectedFqdn = "ch.hearc.jee2024.beerstore.controllers.ManufacturerController";
        String orderControllerExpectedFqdn = "ch.hearc.jee2024.beerstore.controllers.OrderController";
        String userControllerExpectedFqdn = "ch.hearc.jee2024.beerstore.controllers.UserController";

        ArrayList<String> controllers = new ArrayList<>();

        controllers.add(beerControllerExpectedFqdn);
        controllers.add(manufacturerControllerExpectedFqdn);
        controllers.add(orderControllerExpectedFqdn);
        controllers.add(userControllerExpectedFqdn);

        try {
            for (String controllerName : controllers) {
                Class<?> controller = Class.forName(controllerName);
                boolean isAnnotation = controller.isAnnotationPresent(RestController.class);
                assertTrue(isAnnotation, String.format("Controller %s is not a RestController", controller));
            }
        } catch (ClassNotFoundException ex) {
            fail(String.format("Controller %s doesn't exist", beerControllerExpectedFqdn));
        }
    }

    @Test
    public void callBeerShouldCreateBeer() throws Exception {
        this.mvc.perform(post("/manufacturer")
                        .contentType("application/json")
                        .content("{\"name\":\"Test Brewery\",\"country\":\"Test country\"}")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\":\"Test Brewery\",\"country\":\"Test country\"}"));

        this.mvc.perform(post("/beer")
                        .contentType("application/json")
                        .content("{\"manufacturer\":{\"id\": 1},\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5, \"stock\": 10}")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"manufacturer\":{\"id\":1},\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5, \"stock\": 10}"));
    }

    @Test
    public void callBeersShouldReturnBeers() throws Exception {
        // Mock the beerService.list method to return a non-null Page
        Pageable pageable = PageRequest.of(0, 2);
        ManufacturerEntity manufacturer = new ManufacturerEntity("Test Brewery", "Test country");
        manufacturer.setId(1L);

        BeerEntity beer = new BeerEntity(manufacturer, "Test Beer", "A test beer description.", 6.0, 3.0, 10);
        BeerEntity beer2 = new BeerEntity(manufacturer, "Test Beer 2", "A test beer description.", 5.0, 2.5, 10);

        List<BeerEntity> beers = List.of(beer, beer2);
        Page<BeerEntity> beerPage = new PageImpl<>(beers, pageable, beers.size());

        Mockito.when(beerService.list(pageable)).thenReturn(beerPage);

        this.mvc.perform(get("/beer")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{\"manufacturer\":{\"id\":1},\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":6.0,\"price\":3.0, \"stock\": 10}," +
                        "{\"manufacturer\":{\"id\":1},\"name\":\"Test Beer 2\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":2.5, \"stock\": 10}" +
                        "]"));
    }


    @Test
    public void callBeerWithIdShouldReturnBeer() throws Exception {
        ManufacturerEntity manufacturer = new ManufacturerEntity("Test Brewery", "Test country");
        manufacturer.setId(1L);

        BeerEntity beer = new BeerEntity(manufacturer, "Test Beer", "A test beer description.", 6.0, 3.0, 10);
        beer.setId(1L);

        Mockito.when(beerService.get(1L)).thenReturn(Optional.of(beer));

        this.mvc.perform(get("/beer/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"manufacturer\":{\"id\":1},\"name\":\"Test Beer\",\"description\":\"A test beer description.\",\"alcohol\":6.0,\"price\":3.0, \"stock\": 10}"));
    }


    @Test
    public void callBeerWithIdShouldReturnNotFound() throws Exception {
        this.mvc.perform(get("/beer/4"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void callBeerWithIdShouldUpdateBeer() throws Exception {
        ManufacturerEntity manufacturer = new ManufacturerEntity("Test Brewery", "Test country");
        manufacturer.setId(1L);

        BeerEntity beer = new BeerEntity(manufacturer, "Test Beer", "A test beer description.", 6.0, 3.0, 10);
        beer.setId(1L);

        Mockito.when(beerService.get(1L)).thenReturn(Optional.of(beer));

        this.mvc.perform(put("/beer/1")
                        .contentType("application/json")
                        .content("{\"manufacturer\":{\"id\": 1},\"name\":\"Updated Beer\",\"description\":\"An updated test beer description.\",\"alcohol\":5.0,\"price\":3.5, \"stock\": 10}")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"manufacturer\":{\"id\":1},\"name\":\"Updated Beer\",\"description\":\"An updated test beer description.\",\"alcohol\":5.0,\"price\":3.5, \"stock\": 10}"));
    }

    @Test
    public void callBeerWithIdShouldReturnNotFoundOnUpdate() throws Exception {
        this.mvc.perform(put("/beer/999")
                        .header("Authorization", "Basic " + admin)
                        .contentType("application/json")
                        .content("{\"manufacturer\":{\"id\": 1},\"name\":\"Non-existent Beer\",\"description\":\"A test beer description.\",\"alcohol\":5.0,\"price\":3.5}"))
                .andExpect(status().isNotFound());
    }
    @Test
    public void callBeerWithIdShouldDeleteBeer() throws Exception {
        ManufacturerEntity manufacturer = new ManufacturerEntity("Test Brewery", "Test country");
        manufacturer.setId(1L);

        BeerEntity beer = new BeerEntity(manufacturer, "Test Beer", "A test beer description.", 6.0, 3.0, 10);
        beer.setId(1L);

        Mockito.when(beerService.get(1L)).thenReturn(Optional.of(beer));

        this.mvc.perform(delete("/beer/1")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isNoContent());

        Mockito.when(beerService.get(1L)).thenReturn(Optional.empty());

        this.mvc.perform(get("/beer/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void callBeerWithIdShouldReturnNotFoundOnDelete() throws Exception {
        this.mvc.perform(delete("/beer/999")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isNotFound());
    }



    @Test
    public void callManufacturerShouldCreateManufacturer() throws Exception {
        this.mvc.perform(post("/manufacturer")
                        .contentType("application/json")
                        .content("{\"name\":\"Test Brewery\",\"country\":\"Test country\"}")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\":\"Test Brewery\",\"country\":\"Test country\"}"));
    }

    @Test
    public void callManufacturersShouldReturnManufacturers() throws Exception {
        // Mock the manufacturerService.list method to return a non-null Page
        Pageable pageable = PageRequest.of(0, 2);
        ManufacturerEntity manufacturer = new ManufacturerEntity("Test Brewery", "Test country");
        ManufacturerEntity manufacturer2 = new ManufacturerEntity("Test Brewery 2", "Test country 2");

        List<ManufacturerEntity> manufacturers = List.of(manufacturer, manufacturer2);
        Page<ManufacturerEntity> manufacturerPage = new PageImpl<>(manufacturers, pageable, manufacturers.size());

        Mockito.when(manufacturerService.list(pageable)).thenReturn(manufacturerPage);

        this.mvc.perform(get("/manufacturer")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{\"name\":\"Test Brewery\",\"country\":\"Test country\"}," +
                        "{\"name\":\"Test Brewery 2\",\"country\":\"Test country 2\"}" +
                        "]"));
    }

    @Test
    public void callManufacturerWithIdShouldReturnManufacturer() throws Exception {
        ManufacturerEntity manufacturer = new ManufacturerEntity("Test Brewery", "Test country");
        manufacturer.setId(1L);

        Mockito.when(manufacturerService.get(1L)).thenReturn(Optional.of(manufacturer));

        this.mvc.perform(get("/manufacturer/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Test Brewery\",\"country\":\"Test country\"}"));
    }

    @Test
    public void callManufacturerWithIdShouldReturnNotFound() throws Exception {
        this.mvc.perform(get("/manufacturer/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void callManufacturerWithIdShouldUpdateManufacturer() throws Exception {
        ManufacturerEntity manufacturer = new ManufacturerEntity("Test Brewery", "Test country");
        manufacturer.setId(1L);

        Mockito.when(manufacturerService.get(1L)).thenReturn(Optional.of(manufacturer));

        this.mvc.perform(put("/manufacturer/1")
                        .contentType("application/json")
                        .content("{\"name\":\"Updated Brewery\",\"country\":\"Updated country\"}")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Updated Brewery\",\"country\":\"Updated country\"}"));
    }

    @Test
    public void callManufacturerWithIdShouldReturnNotFoundOnUpdate() throws Exception {
        this.mvc.perform(put("/manufacturer/999")
                        .header("Authorization", "Basic " + admin)
                        .contentType("application/json")
                        .content("{\"name\":\"Non-existent Brewery\",\"country\":\"Test country\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void callManufacturerWithIdShouldDeleteManufacturer() throws Exception {
        ManufacturerEntity manufacturer = new ManufacturerEntity("Test Brewery", "Test country");
        manufacturer.setId(1L);

        Mockito.when(manufacturerService.get(1L)).thenReturn(Optional.of(manufacturer));

        this.mvc.perform(delete("/manufacturer/1")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isNoContent());

        Mockito.when(manufacturerService.get(1L)).thenReturn(Optional.empty());

        this.mvc.perform(get("/manufacturer/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void callManufacturerWithIdShouldReturnNotFoundOnDelete() throws Exception {
        this.mvc.perform(delete("/manufacturer/999")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isNotFound());
    }

    @Test
    public void callUserShouldCreateUser() throws Exception {
        this.mvc.perform(post("/user")
                        .contentType("application/json")
                        .content("{\"username\":\"test\",\"password\":\"test\",\"role\":\"USER\"}")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"username\":\"test\",\"role\":\"USER\"}"));
    }

    @Test
    public void callUsersShouldReturnUsers() throws Exception {
        // Mock the userService.list method to return a non-null Page
        Pageable pageable = PageRequest.of(0, 2);
        UserEntity user = new UserEntity("test", "test", UserEntity.Role.USER);
        UserEntity user2 = new UserEntity("test2", "test2", UserEntity.Role.USER);

        List<UserEntity> users = List.of(user, user2);
        Page<UserEntity> userPage = new PageImpl<>(users, pageable, users.size());

        Mockito.when(userService.list(pageable)).thenReturn(userPage);

        this.mvc.perform(get("/user")
                        .header("Authorization", "Basic " + admin)
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{\"username\":\"test\",\"role\":\"USER\"}," +
                        "{\"username\":\"test2\",\"role\":\"USER\"}" +
                        "]"));
    }

    @Test
    public void callUserWithIdShouldReturnUser() throws Exception {
        UserEntity user = new UserEntity("test", "test", UserEntity.Role.USER);
        user.setId(1L);

        Mockito.when(userService.get(1L)).thenReturn(Optional.of(user));

        this.mvc.perform(get("/user/1")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"username\":\"test\",\"role\":\"USER\"}"));
    }

    @Test
    public void callUserWithIdShouldReturnNotFound() throws Exception {
        this.mvc.perform(get("/user/999")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isNotFound());
    }


    @Test
    public void callUserWithIdShouldUpdateUser() throws Exception {
        UserEntity user = new UserEntity("test", "test", UserEntity.Role.USER);
        user.setId(1L);

        Mockito.when(userService.get(1L)).thenReturn(Optional.of(user));

        this.mvc.perform(put("/user/1")
                        .contentType("application/json")
                        .content("{\"username\":\"updated\",\"role\":\"USER\"}")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"username\":\"updated\",\"role\":\"USER\"}"));
    }

    @Test
    public void callUserWithIdShouldReturnNotFoundOnUpdate() throws Exception {
        this.mvc.perform(put("/user/999")
                        .header("Authorization", "Basic " + admin)
                        .contentType("application/json")
                        .content("{\"username\":\"non-existent\",\"role\":\"USER\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void callUserWithIdShouldDeleteUser() throws Exception {
        UserEntity user = new UserEntity("test", "test", UserEntity.Role.USER);
        user.setId(1L);

        Mockito.when(userService.get(1L)).thenReturn(Optional.of(user));

        this.mvc.perform(delete("/user/1")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isNoContent());

        Mockito.when(userService.get(1L)).thenReturn(Optional.empty());
    }

    @Test
    public void callUserWithIdShouldReturnNotFoundOnDelete() throws Exception {
        this.mvc.perform(delete("/user/999")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isNotFound());
    }

    @Test
    public void callOrderShouldCreateOrder() throws Exception {
        //use mockito, requires manufacturer and beers first
        ManufacturerEntity manufacturer = new ManufacturerEntity("Test Brewery", "Test country");
        manufacturer.setId(1L);

        BeerEntity beer = new BeerEntity(manufacturer, "Test Beer", "A test beer description.", 6.0, 3.0, 10);
        beer.setId(1L);

        BeerEntity beer2 = new BeerEntity(manufacturer, "Test Beer 2", "A test beer description.", 5.0, 2.5, 10);
        beer2.setId(2L);

        Mockito.when(beerService.get(1L)).thenReturn(Optional.of(beer));
        Mockito.when(beerService.get(2L)).thenReturn(Optional.of(beer2));

        this.mvc.perform(post("/order")
                        .contentType("application/json")
                        .content("{\"orderBeers\":[{\"beer\":{\"id\":1},\"quantity\":2},{\"beer\":{\"id\":2},\"quantity\":1}]}")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"orderBeers\":[{\"beer\":{\"id\":1},\"quantity\":2},{\"beer\":{\"id\":2},\"quantity\":1}], \"totalPrice\":8.5}"));
    }

    @Test
    public void callOrdersShouldReturnOrders() throws Exception {
        // Mock the orderService.list method to return a non-null Page
        Pageable pageable = PageRequest.of(0, 2);
        ManufacturerEntity manufacturer = new ManufacturerEntity("Test Brewery", "Test country");
        manufacturer.setId(1L);

        BeerEntity beer = new BeerEntity(manufacturer, "Test Beer", "A test beer description.", 6.0, 3.0, 10);
        beer.setId(1L);

        BeerEntity beer2 = new BeerEntity(manufacturer, "Test Beer 2", "A test beer description.", 5.0, 2.5, 10);
        beer2.setId(2L);

        OrderEntity order = new OrderEntity(Set.of(new OrderBeerEntity(beer, 2), new OrderBeerEntity(beer2, 1)));
        order.setId(1L);

        OrderEntity order2 = new OrderEntity(Set.of(new OrderBeerEntity(beer, 1), new OrderBeerEntity(beer2, 2)));
        order2.setId(2L);

        List<OrderEntity> orders = List.of(order, order2);
        Page<OrderEntity> orderPage = new PageImpl<>(orders, pageable, orders.size());

        Mockito.when(orderService.list(pageable)).thenReturn(orderPage);

        this.mvc.perform(get("/order")
                        .param("page", "0")
                        .param("size", "2")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{\"orderBeers\":[{\"beer\":{\"id\":1},\"quantity\":2},{\"beer\":{\"id\":2},\"quantity\":1}], \"totalPrice\":8.5}," +
                        "{\"orderBeers\":[{\"beer\":{\"id\":1},\"quantity\":1},{\"beer\":{\"id\":2},\"quantity\":2}], \"totalPrice\":8.0}" +
                        "]"));
    }

    @Test
    public void callOrderWithIdShouldReturnOrder() throws Exception {
        ManufacturerEntity manufacturer = new ManufacturerEntity("Test Brewery", "Test country");
        manufacturer.setId(1L);

        BeerEntity beer = new BeerEntity(manufacturer, "Test Beer", "A test beer description.", 6.0, 3.0, 10);
        beer.setId(1L);

        BeerEntity beer2 = new BeerEntity(manufacturer, "Test Beer 2", "A test beer description.", 5.0, 2.5, 10);
        beer2.setId(2L);

        UserEntity user = new UserEntity("test", "test", UserEntity.Role.USER);
        user.setId(1L);

        OrderEntity order = new OrderEntity(Set.of(new OrderBeerEntity(beer, 2), new OrderBeerEntity(beer2, 1)));
        order.setId(1L);
        order.setUser(user);

        Mockito.when(orderService.get(1L)).thenReturn(Optional.of(order));

        this.mvc.perform(get("/order/1")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"orderBeers\":[{\"beer\":{\"id\":1},\"quantity\":2},{\"beer\":{\"id\":2},\"quantity\":1}], \"totalPrice\":8.5}"));
    }

    @Test
    public void callOrderWithIdShouldReturnNotFound() throws Exception {
        this.mvc.perform(get("/order/999")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isNotFound());
    }

    @Test
    public void callOrderWithIdShouldDeleteOrder() throws Exception {
        ManufacturerEntity manufacturer = new ManufacturerEntity("Test Brewery", "Test country");
        manufacturer.setId(1L);

        BeerEntity beer = new BeerEntity(manufacturer, "Test Beer", "A test beer description.", 6.0, 3.0, 10);
        beer.setId(1L);

        BeerEntity beer2 = new BeerEntity(manufacturer, "Test Beer 2", "A test beer description.", 5.0, 2.5, 10);
        beer2.setId(2L);

        UserEntity user = new UserEntity("test", "test", UserEntity.Role.USER);
        user.setId(1L);

        OrderEntity order = new OrderEntity(Set.of(new OrderBeerEntity(beer, 2), new OrderBeerEntity(beer2, 1)));
        order.setId(1L);
        order.setUser(user);

        Mockito.when(orderService.get(1L)).thenReturn(Optional.of(order));

        this.mvc.perform(delete("/order/1")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isNoContent());

        Mockito.when(orderService.get(1L)).thenReturn(Optional.empty());
    }

    @Test
    public void callOrderWithIdShouldReturnNotFoundOnDelete() throws Exception {
        this.mvc.perform(delete("/order/999")
                        .header("Authorization", "Basic " + admin))
                .andExpect(status().isNotFound());
    }
}
