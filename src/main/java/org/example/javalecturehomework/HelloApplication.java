package org.example.javalecturehomework;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.javalecturehomework.utils.SceneLoader;

import java.io.IOException;



public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        SceneLoader.setPrimaryStage(primaryStage);
        SceneLoader.loadScene("main-menu.fxml", "Main Menu");
    }

    public static void main(String[] args) {
        launch();
    }
}