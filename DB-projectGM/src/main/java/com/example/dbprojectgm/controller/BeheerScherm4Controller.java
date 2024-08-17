package com.example.dbprojectgm.controller;

import com.example.dbprojectgm.DB_ProjectMain;
import com.example.dbprojectgm.models.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BeheerScherm4Controller   {
    public TableColumn<Item, Integer> column1;
    public TableColumn<Item,String> column2;
    public TableColumn<Item,Double> column3;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnClose;
    @FXML
    private TableView<Item> tblConfigs;
    @FXML
    private ComboBox<String> cmbLocationFilter;

    private UserManager userManager;
    private GameManager gameManager;
    private MiscManager miscManager;
    private LocationManager locationManager;



    @FXML
    public void initialize() throws SQLException {
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));
        column3.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadLocations();

        cmbLocationFilter.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                loadItemsByLocation(newValue);

            }
        });

        loadItems();
        btnDelete.setOnAction(event -> deleteSelectedItem());
        btnAdd.setOnAction(event -> addNewItem());
    }

    private void loadLocations() throws SQLException {
        List<String> locations = new ArrayList<>();
        locations.add("All");
        locations.addAll(gameManager.getAllLocationNames());
        ObservableList<String> observableList = FXCollections.observableArrayList(locations);
        cmbLocationFilter.setItems(observableList);
        cmbLocationFilter.getSelectionModel().selectFirst();
    }


    @FXML
    private void deleteSelectedItem() {
        Item selectedItem = tblConfigs.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Are you sure you want to delete the selected item?");
            alert.setContentText("This action cannot be undone.");

            if (alert.showAndWait().get() == ButtonType.OK) {
                boolean success = false;
                if (selectedItem instanceof Game) {
                    success = gameManager.deleteGameById(selectedItem.getId());
                } else if (selectedItem instanceof Misc) {
                    success = miscManager.deleteMiscById(selectedItem.getId());
                }

                if (success) {
                    tblConfigs.getItems().remove(selectedItem);
                    showAlert("Item deleted successfully.", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Failed to delete the item.", Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("No item selected. Please select an item to delete.", Alert.AlertType.WARNING);
        }
    }


       private void loadItems(){
           List<Item> items = new ArrayList<>();
           items.addAll(gameManager.getAllGames());
           items.addAll(miscManager.getAllMisc());
           ObservableList<Item> observableList = FXCollections.observableArrayList(items);
           tblConfigs.setItems(observableList);



}

        private void loadItemsByLocation(String locationName){

            List<Item> items = new ArrayList<>();
            if(locationName.equals("All")){
                loadItems();
            }else {
                Location location = locationManager.getLocation(locationName);
                items.addAll(gameManager.getGamesFromLocation(location));
                items.addAll(miscManager.getMiscFromLocation(location));
                ObservableList<Item> observableList = FXCollections.observableArrayList(items);
                tblConfigs.setItems(observableList);
            }
        }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }
    @FXML
    private void addNewItem(){
        NavigationHelper.goToBeheerScherm1(false);
        }
    }



