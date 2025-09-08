package com.ocart.orange_cart.controller;

import javafx.animation.PauseTransition;
import com.ocart.orange_cart.model.ShoppingCart;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Controller for the fake payment gateway.
 * UPDATED: Uses a more robust Task structure with event handlers to guarantee UI responsiveness.
 */
public class PaymentGatewayController {

    @FXML
    private TextField cardNumberField;
    @FXML
    private TextField expiryDateField;
    @FXML
    private TextField cvvField;
    @FXML
    private Button payButton;
    @FXML
    private Label paymentStatusLabel;
    @FXML
    private VBox processingPane;
    @FXML
    private VBox rootPane;

    private double amount;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "NG"));

    private boolean paymentSuccessful = false;

    public boolean isPaymentSuccessful() {
        return paymentSuccessful;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        payButton.setText("Pay " + currencyFormatter.format(this.amount));
    }

    @FXML
    private void initialize() {
        System.out.println("[PaymentGateway] initialize() on FX thread=" + javafx.application.Platform.isFxApplicationThread());
    }

    @FXML
    private void handlePayNow(ActionEvent event) {
        // Validation
        if (cardNumberField.getText().trim().isEmpty() ||
                expiryDateField.getText().trim().isEmpty() ||
                cvvField.getText().trim().isEmpty()) {
            showStatus("Please fill in all fields.", javafx.scene.paint.Color.RED);
            return;
        }

        // Disable UI interactions during processing
        payButton.setDisable(true);
        setNodeVisible(processingPane, true);
        setNodeVisible(paymentStatusLabel, false);

        System.out.println("[PaymentGateway] Starting payment on FX thread? " + javafx.application.Platform.isFxApplicationThread());

        // Background task simulating payment processing (non-blocking to FX thread)
        Task<Void> paymentTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Simulate incremental work & allow cancellation points
                int steps = 6; // 6 * 500ms = 3s
                for (int i = 0; i < steps; i++) {
                    if (isCancelled()) return null;
                    Thread.sleep(500);
                }
                return null;
            }
        };

        paymentTask.setOnSucceeded(e -> {
            // Clear cart items since payment succeeded
            ShoppingCart.getItems().clear();
            setNodeVisible(processingPane, false);
            showStatus("Payment Successful!", javafx.scene.paint.Color.GREEN);
            paymentSuccessful = true;

            PauseTransition closeDelay = new PauseTransition(Duration.seconds(2));
            closeDelay.setOnFinished(ev -> {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.close();
            });
            closeDelay.play();
        });

        paymentTask.setOnFailed(e -> {
            setNodeVisible(processingPane, false);
            Throwable ex = paymentTask.getException();
            System.err.println("[PaymentGateway] Payment task failed: " + (ex != null ? ex.getMessage() : "unknown"));
            showStatus("Payment failed.", javafx.scene.paint.Color.RED);
            payButton.setDisable(false);
        });

        paymentTask.setOnCancelled(e -> {
            setNodeVisible(processingPane, false);
            showStatus("Payment cancelled.", javafx.scene.paint.Color.ORANGE);
            payButton.setDisable(false);
        });

        Thread worker = new Thread(paymentTask, "payment-task-thread");
        worker.setDaemon(true); // Daemon so it won't block app exit
        worker.start();
    }

    /**
     * Helper to toggle both visibility and management.
     */
    private void setNodeVisible(javafx.scene.Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    /**
     * Helper to show the status label with message + color.
     */
    private void showStatus(String message, javafx.scene.paint.Color color) {
        paymentStatusLabel.setText(message);
        paymentStatusLabel.setTextFill(color);
        setNodeVisible(paymentStatusLabel, true);
    }
}


