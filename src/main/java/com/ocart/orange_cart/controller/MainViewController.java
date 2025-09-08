package com.ocart.orange_cart.controller;


import com.ocart.orange_cart.db.DatabaseManager;
import com.ocart.orange_cart.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * The 'Controller' in our MVC architecture.
 * It connects the MainView.fxml (View) with the application's
 * business logic and data (Model).
 */
public class MainViewController implements Initializable {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> descriptionColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, Integer> stockColumn;

    // An ObservableList is used to hold the products.
    // Any changes to this list will automatically update the TableView.
    private ObservableList<Product> productList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // This method is called after the FXML file has been loaded.
        // It's the perfect place to set up the view.
        setupTableColumns();
        loadProducts(); // We'll load dummy data for now.
        productTable.setItems(productList);
    }

    private void setupTableColumns() {
        // This binds the columns in the TableView to the properties of the Product model.
        // The string "id", "name", etc., must match the property names in the Product class.
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    /**
     * Loads products into the productList.
     * For now, it just loads dummy data.
     */
    private void loadProducts() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String sql = "SELECT id, name, description, price, stock FROM products";
                try (Connection conn = DatabaseManager.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        String description = rs.getString("description");
                        double price = rs.getDouble("price");
                        int stock = rs.getInt("stock");

                        Product p = new Product(id, name, description, price, stock, "https://placehold.co/200x200/F7941D/white?text=Phone+1");
                        javafx.application.Platform.runLater(() -> productList.add(p));
                    }
                }
                return null;
            }
        };
        task.setOnFailed(e -> System.err.println("[MainView] Load products failed: " + task.getException()));
        Thread t = new Thread(task, "main-load-products-task");
        t.setDaemon(true);
        t.start();
    }
}
