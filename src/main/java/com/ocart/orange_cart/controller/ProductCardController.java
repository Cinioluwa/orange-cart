package com.ocart.orange_cart.controller;

import com.ocart.orange_cart.model.Product;
import com.ocart.orange_cart.model.ShoppingCart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Controller for a single ProductCard.fxml component.
 * Each product card in the grid will have its own instance of this controller.
 */
public class ProductCardController {

    @FXML
    private ImageView productImageView;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label productPriceLabel;

    @FXML
    private Button addToCartButton;

    private Product product;

    /**
     * This method is called by the CustomerViewController to inject the
     * product data into this specific card.
     */
    public void setProduct(Product product) {
        this.product = product;
        productNameLabel.setText(product.getName());

        // Format the price to include currency symbol and commas
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "NG"));
        productPriceLabel.setText(currencyFormatter.format(product.getPrice()));

        // Load the image. Use a placeholder if the image fails to load.
        Image image = new Image(product.getImageUrl(), true); // true = load in background
        productImageView.setImage(image);
        image.errorProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // If there's an error, set a placeholder image
                productImageView.setImage(new Image("https://placehold.co/200x200/cccccc/333333?text=No+Image"));
            }
        });
    }

    /**
     * Handles the click event for the "Add to Cart" button.
     */
    @FXML
    void handleAddToCart(ActionEvent event) {
        if (product != null) {
            ShoppingCart.addProduct(this.product);
            // Optional: Visually confirm the action
            addToCartButton.setText("Added!");
            // You could use a PauseTransition to revert the text after a second
        }
    }
}