module com.ocart.orange_cart {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics; // Added explicitly for Stage & Scene classes accessibility
    requires com.zaxxer.hikari;
    requires java.sql;

    opens com.ocart.orange_cart.controller to javafx.fxml;
    opens com.ocart.orange_cart.model to javafx.base;

    exports com.ocart.orange_cart;
    exports com.ocart.orange_cart.controller;
    exports com.ocart.orange_cart.model;
}
