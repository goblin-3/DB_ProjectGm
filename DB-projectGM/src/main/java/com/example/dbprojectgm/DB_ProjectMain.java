package com.example.dbprojectgm;


import com.example.dbprojectgm.controller.MyController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class DB_ProjectMain extends Application {

private static Scene scene;


    public static void setScene(String fxmlfile, int width, int height, MyController controller ) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(fxmlfile));
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        scene.setRoot(root);
        scene.getWindow().setWidth(width);
        scene.getWindow().setHeight(height);
    }

    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlLocation = getClass().getResource("/com/example/dbprojectgm/home.fxml");
        if (fxmlLocation == null) {
            System.err.println("FXML file not found!");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        scene = new Scene(fxmlLoader.load(), 800, 800);
        stage.setTitle("DB");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}