package com.example.dbprojectgm.controller;

import com.example.dbprojectgm.DB_ProjectMain;
import com.example.dbprojectgm.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BeheerScherm5Controller {
    @FXML
    private TextField LNameFld;

    @FXML
    private TextField FNameFld;

    @FXML
    private TextField emailFld;

    @FXML
    private TextField PasswordFld;

    @FXML
    private TextField UsernameFld;

    @FXML
    private Button RegisterBtn;

    @FXML
    private Text resultText;

    @FXML
    private CheckBox box;
    @FXML
    private CheckBox box1;

    @FXML
    public void initialize() {
        RegisterBtn.setOnAction(event -> registerUser());
    }

    private void registerUser() {
        String lastName = LNameFld.getText().trim();
        String firstName = FNameFld.getText().trim();
        String email = emailFld.getText().trim();
        String username = UsernameFld.getText().trim();
        String password = PasswordFld.getText().trim();
        boolean isEmployee = box.isSelected();
        boolean isAdmin = box1.isSelected();

        if (lastName.isEmpty() || firstName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert("Error",  Alert.AlertType.ERROR);
            return;
        }
        String query = "INSERT INTO Customer (First_Name, Last_Name, Email, Password, Username, Is_Employee, Is_Admin) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, username);
            pstmt.setBoolean(6, isEmployee);
            pstmt.setBoolean(7, isAdmin);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success",  Alert.AlertType.INFORMATION);
                var resourceName = "beheerscherm"+3+".fxml";
                try {
                    DB_ProjectMain.setScene(resourceName,800,800);
                } catch (IOException e){
                    throw new RuntimeException("failed to load stage :"+ 3,e);
                }
            } else {
                showAlert("Error", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", Alert.AlertType.ERROR);
        }


    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }


    //back to stage 3
}
