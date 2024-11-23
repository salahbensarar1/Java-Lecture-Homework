package org.example.javalecturehomework.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.javalecturehomework.utils.SceneLoader;

public class MainMenuController {
    @FXML
    public void onRead() {
        SceneLoader.loadScene("read-view.fxml", "Read Data");
    }

    @FXML
    public void onReadWithFilter() {
        SceneLoader.loadScene("read2-view.fxml", "Filter Data");
    }

    @FXML
    public void onWrite() {
        SceneLoader.loadScene("write-view.fxml", "Add New Data");
    }

    @FXML
    public void onChange() {
        SceneLoader.loadScene("change-view.fxml", "Update Data");
    }

    @FXML
    public void onDelete() {
        SceneLoader.loadScene("delete-view.fxml", "Delete Data");
    }
}
