module org.example.javalecturehomework {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires jakarta.jws;
    requires jakarta.xml.ws;
    requires jakarta.xml.bind; // Jakarta JAXB API
    requires com.sun.xml.bind; // JAXB Runtime
    requires org.jvnet.staxex; // StAX extensions
    requires com.sun.xml.fastinfoset; // Fast Infoset implementation
    requires java.naming;
    requires jakarta.persistence;
    requires java.desktop;
    requires org.slf4j;

    // Optional libraries
    requires com.almasb.fxgl.all;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires com.dlsc.formsfx;
    requires v20;

    // Exports
    exports org.example.javalecturehomework;
    exports org.example.javalecturehomework.controller;
    exports org.example.javalecturehomework.service to com.sun.xml.ws;

    // Opens for reflection
    opens org.example.javalecturehomework to javafx.fxml;
    opens org.example.javalecturehomework.controller to javafx.fxml;
    opens org.example.javalecturehomework.service to jakarta.xml.bind, com.sun.xml.bind; // Open to both JAXB modules
}

//module org.example.javalecturehomework {
//    requires javafx.controls;
//    requires javafx.fxml;
//    requires javafx.graphics;
//    opens org.example.javalecturehomework.model to javafx.base;
//    requires java.sql;
//    requires jakarta.jws;
//    requires com.almasb.fxgl.all;
//    requires jakarta.xml.ws;
//    requires  com.sun.xml.bind;
//    requires org.kordamp.ikonli.javafx;
//    requires com.sun.xml.fastinfoset;
//    requires jakarta.persistence;
//    requires com.sun.xml.ws;
//    requires com.dlsc.formsfx;
//    requires jakarta.xml.bind; // Using Jakarta JAXB (instead of java.xml.bind)
//    requires java.naming;
//    requires net.synedra.validatorfx;
//    requires org.jvnet.staxex;
//    requires java.desktop;
//    requires org.slf4j;
//    requires v20;
//    // Exports
//    exports org.example.javalecturehomework; // Export the main package, including HelloApplication
//    exports org.example.javalecturehomework.controller; // Export the controller package for FXMLLoader
//
//    // Opens for reflection
//    opens org.example.javalecturehomework to javafx.fxml; // Allow reflection for FXML files
//    opens org.example.javalecturehomework.controller to javafx.fxml; // Allow reflection in controllers
//    opens org.example.javalecturehomework.service to jakarta.xml.bind;
//    exports org.example.javalecturehomework.service to com.sun.xml.ws;
//
//
//
//}
//
