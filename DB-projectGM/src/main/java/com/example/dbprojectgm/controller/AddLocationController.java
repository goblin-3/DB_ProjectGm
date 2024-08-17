package com.example.dbprojectgm.controller;

import com.example.dbprojectgm.DatabaseManager;
import com.example.dbprojectgm.models.Location;
import com.example.dbprojectgm.models.Museum;
import com.example.dbprojectgm.models.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddLocationController {

    @FXML
    private ComboBox<String> cmbLocationType;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtCashReserve;

    @FXML
    private TextField txtAddress;

    @FXML
    private Button btnAddLocation;

    @FXML
    private Button btnCancel;


    @FXML
    public void initialize() {
        cmbLocationType.getItems().addAll("Warehouse", "Museum");
        btnAddLocation.setOnAction(event -> addLocation());
    }


    private void addLocation(){

        String selectedLocationType = cmbLocationType.getSelectionModel().getSelectedItem();
        if (selectedLocationType == null) {
            showAlert("Error",  Alert.AlertType.ERROR);
            return;
        }

        String name = txtName.getText().trim();
        String cashReserveStr = txtCashReserve.getText().trim();
        String address = txtAddress.getText().trim();

        if (name.isEmpty() || cashReserveStr.isEmpty() || address.isEmpty()) {
            showAlert("Error", Alert.AlertType.ERROR);
            return;
        }
        double cashReserve;
        try {
            cashReserve = Double.parseDouble(cashReserveStr);
        } catch (NumberFormatException e) {
            showAlert("Error",  Alert.AlertType.ERROR);
            return;
        }

        Location location;
        if (selectedLocationType.equals("Warehouse")) {
            location = new Warehouse(name, cashReserve, address);
        } else {
            location = new Museum(name, cashReserve, address);
        }
        try {
            if (createLocation(selectedLocationType.equals("Warehouse"), location)) {
                showAlert("Success",  Alert.AlertType.INFORMATION);
                closeWindow();
            } else {
                showAlert("Error",  Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error: " + e.getMessage(), Alert.AlertType.ERROR);
        }



    }
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }

    private boolean createLocation(boolean isWarehouse, Location location) throws SQLException {
        String query;

        if (isWarehouse) {
            query = "INSERT INTO Warehouse (Name, Cash_Reserve, Location) VALUES (?, ?, ?)";
        } else {
            query = "INSERT INTO Museum (Name, Cash_Reserve, Location) VALUES (?, ?, ?)";
        }

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, location.getName());
            pstmt.setDouble(2, location.getCash_reserve());
            pstmt.setString(3, location.getAdress());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
