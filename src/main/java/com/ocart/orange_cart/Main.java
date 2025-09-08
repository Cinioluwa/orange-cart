package com.ocart.orange_cart;

import javafx.application.Application;
import com.ocart.orange_cart.util.PulseMonitor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main entry point for the Orange Cart application.
 * It loads the primary FXML view and displays it.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/com/ocart/orange_cart/view/CustomerView.fxml"));
    // Start pulse monitor to verify FX thread isn't blocked
    PulseMonitor.start();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Orange Cart");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

