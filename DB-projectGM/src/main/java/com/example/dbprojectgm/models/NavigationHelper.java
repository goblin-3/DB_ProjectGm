package com.example.dbprojectgm.models;

import com.example.dbprojectgm.DB_ProjectMain;
import com.example.dbprojectgm.controller.BeheerScherm1Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationHelper {

    public static void goToBeheerScherm1(boolean isAdmin) {
        try {
            FXMLLoader loader = new FXMLLoader(DB_ProjectMain.class.getResource("beheerscherm1.fxml"));
            Parent root = loader.load();

            BeheerScherm1Controller controller = loader.getController();
            controller.setAdmin(isAdmin);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 800));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
