package com.ocart.orange_cart.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A simple singleton class to manage the state of the shopping cart.
 * An ObservableList is used so that UI components can "listen" for changes
 * (like adding a product) and update themselves automatically.
 */
public class ShoppingCart {

    private static final ObservableList<Product> items = FXCollections.observableArrayList();

    private ShoppingCart() {
        // Private constructor to prevent instantiation
    }

    public static ObservableList<Product> getItems() {
        return items;
    }

    public static void addProduct(Product product) {
        // In a real app, you'd handle quantity here
        items.add(product);
        System.out.println("Added to cart: " + product.getName());
        System.out.println("Total items in cart: " + items.size());
    }

    public static void removeProduct(Product product) {
        items.remove(product);
    }
}
