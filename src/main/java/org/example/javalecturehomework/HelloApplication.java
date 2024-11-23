package org.example.javalecturehomework;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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