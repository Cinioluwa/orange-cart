package com.ocart.orange_cart.controller;

import com.ocart.orange_cart.model.Product;
import com.ocart.orange_cart.model.ShoppingCart;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * **UPDATED:** Now handles the checkout process by opening a fake payment gateway.
 */
public class CartViewController implements Initializable {

    @FXML
    private TableView<Product> cartTable;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, Void> actionColumn;
    @FXML
    private Label totalLabel;
    @FXML
    private Button checkoutButton; // Reference to the button

    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "NG"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ... (existing code for setting up columns) ...
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : currencyFormatter.format(price));
            }
        });

        addRemoveButtonToActionColumn();

        cartTable.setItems(ShoppingCart.getItems());
        ShoppingCart.getItems().addListener((ListChangeListener<Product>) change -> updateTotal());

        updateTotal();
    }

    /**
     * **NEW METHOD**
     * Handles the "Proceed to Checkout" button click.
     */
    @FXML
    private void handleProceedToCheckout(ActionEvent event) {
        double total = calculateTotal();
        // Close current stage first
        Stage currentStage = (Stage) checkoutButton.getScene().getWindow();
        currentStage.close();
        // Defer loading & showing new stage to next pulse to avoid potential pulse stall
        javafx.application.Platform.runLater(() -> {
            try {
                System.out.println("[CartView] Opening payment stage on FX thread=" + Thread.currentThread().getName());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocart/orange_cart/view/PaymentGatewayView.fxml"));
                Parent root = loader.load();
                PaymentGatewayController controller = loader.getController();
                controller.setAmount(total);
                Stage paymentStage = new Stage();
                paymentStage.setTitle("Secure Payment");
                paymentStage.initModality(Modality.APPLICATION_MODAL);
                paymentStage.setScene(new Scene(root));
                // Use show() (not showAndWait()) to see if nested event loop caused pulse freeze
                paymentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void addRemoveButtonToActionColumn() {
        // ... (existing code) ...
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");

            {
                removeButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
                removeButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    ShoppingCart.getItems().remove(product);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox pane = new HBox(removeButton);
                    pane.setAlignment(Pos.CENTER);
                    setGraphic(pane);
                }
            }
        });
    }

    private void updateTotal() {
        totalLabel.setText(currencyFormatter.format(calculateTotal()));
    }

    private double calculateTotal() {
        double total = 0.0;
        for (Product item : ShoppingCart.getItems()) {
            total += item.getPrice();
        }
        return total;
    }
}

