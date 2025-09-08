package com.ocart.orange_cart.controller;

import com.ocart.orange_cart.db.DatabaseManager;
import com.ocart.orange_cart.model.Product;
import com.ocart.orange_cart.model.ShoppingCart;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.concurrent.Task;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the main customer-facing view (CustomerView.fxml).
 * **UPDATED:** Now includes a handler to open the cart view.
 */
public class CustomerViewController implements Initializable {

    @FXML
    private TilePane productGrid;

    @FXML
    private Label cartLabel;

    // This method is called when the FXML file is loaded.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProducts();
        ShoppingCart.getItems().addListener((ListChangeListener<Product>) change -> {
            updateCartLabel();
        });
        updateCartLabel();
    }

    /**
     * **THIS IS THE NEW METHOD**
     * Handles the "View Cart" button click. It loads CartView.fxml
     * and displays it in a new, separate window (Stage).
     */
    @FXML
    private void handleViewCart(ActionEvent event) {
        try {
            // Load the FXML file for the cart view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocart/orange_cart/view/CartView.fxml"));
            Parent root = loader.load();

            // Create a new window (Stage) for the cart
            Stage cartStage = new Stage();
            cartStage.setTitle("Your Shopping Cart");

            // This makes the cart window block the main window until it's closed.
            cartStage.initModality(Modality.APPLICATION_MODAL);
            cartStage.setScene(new Scene(root));

            // Show the window and wait for it to be closed before returning
            cartStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            // In a real application, you'd show an error dialog to the user here.
        }
    }


    // --- NO CHANGES BELOW THIS LINE ---

    private void loadProducts() {
        // Run DB fetch off FX thread to avoid UI stalls
        Task<List<Product>> task = new Task<>() {
            @Override
            protected List<Product> call() {
                return fetchProductsFromDB();
            }
        };

        task.setOnSucceeded(e -> {
            List<Product> products = task.getValue();
            productGrid.getChildren().clear();
            for (Product product : products) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocart/orange_cart/view/ProductCard.fxml"));
                    Node productCardNode = loader.load();
                    ProductCardController controller = loader.getController();
                    controller.setProduct(product);
                    productGrid.getChildren().add(productCardNode);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        task.setOnFailed(e -> {
            System.err.println("[CustomerView] Failed to load products: " + task.getException());
        });

        Thread t = new Thread(task, "load-products-task");
        t.setDaemon(true);
        t.start();
    }

    private List<Product> fetchProductsFromDB() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, description, price, stock, image_url FROM products";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("image_url")
                );
                products.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching products from database: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    private void updateCartLabel() {
        int itemCount = ShoppingCart.getItems().size();
        cartLabel.setText("Cart (" + itemCount + ")");
    }
}

