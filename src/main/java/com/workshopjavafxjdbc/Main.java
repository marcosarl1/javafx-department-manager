package com.workshopjavafxjdbc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainView.fxml"));
            scene = new Scene(fxmlLoader.load());
            stage.setTitle("Sample JavaFX application");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        launch();
    }
}