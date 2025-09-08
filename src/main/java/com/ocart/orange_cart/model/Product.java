package com.ocart.orange_cart.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Represents the Product entity (POJO).
 * This is the 'Model' in our MVC architecture. It uses JavaFX Properties
 * (e.g., SimpleStringProperty) to allow easy data binding with UI components
 * like TableView, as highlighted in your guide.
 */
public class Product {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty description;
    private final SimpleDoubleProperty price;
    private final SimpleIntegerProperty stock;
    private final SimpleStringProperty imageUrl;

    public Product(int id, String name, String description, double price, int stock, String imageUrl) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.price = new SimpleDoubleProperty(price);
        this.stock = new SimpleIntegerProperty(stock);
        this.imageUrl = new SimpleStringProperty(imageUrl);
    }

    // --- Getters for JavaFX Properties ---
    public SimpleIntegerProperty idProperty() { return id; }
    public SimpleStringProperty nameProperty() { return name; }
    public SimpleStringProperty descriptionProperty() { return description; }
    public SimpleDoubleProperty priceProperty() { return price; }
    public SimpleIntegerProperty stockProperty() { return stock; }
    public SimpleStringProperty imageUrlProperty() {
        return imageUrl;
    }

    // --- Standard Getters/Setters for values ---
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }

    public int getStock() { return stock.get(); }
    public void setStock(int stock) { this.stock.set(stock); }

    public String getImageUrl() { return imageUrl.get(); }
    public void setImageUrl(String imageUrl) {
        this.imageUrl.set(imageUrl);
    }

}
