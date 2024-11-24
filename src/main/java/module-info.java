module org.example.javalecturehomework {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;

    exports org.example.javalecturehomework; // Export the main package, including HelloApplication
    exports org.example.javalecturehomework.controller; // Export the controller package for FXMLLoader

    opens org.example.javalecturehomework to javafx.fxml; // Allow reflection for FXML files
    opens org.example.javalecturehomework.controller to javafx.fxml; // Allow reflection in controllers
}
