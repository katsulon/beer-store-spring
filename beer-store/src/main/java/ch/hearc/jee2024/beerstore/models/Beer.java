package ch.hearc.jee2024.beerstore.models;

public record Beer(Integer id, String manufacturer, String name, String description, double alcohol, double price) {
}
