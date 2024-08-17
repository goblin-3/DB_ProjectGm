package com.example.dbprojectgm.controller;

import com.example.dbprojectgm.DB_ProjectMain;
import com.example.dbprojectgm.models.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BeheerScherm1Controller  {

@FXML
    public CheckBox chkbx;
@FXML
    public TextField name;
@FXML
    public ComboBox<String> year;
@FXML
public ComboBox<String> publi;
@FXML
public ComboBox<String> genre;
@FXML
public ComboBox<String> cons;
@FXML
    public TextField name1;
    @FXML
    public ComboBox<String> year1;
    @FXML
    public ComboBox<String> publi1;
    @FXML
    public ComboBox<String> genre1;
    @FXML
    public ComboBox<String> type;
    @FXML
    public Button btn;
    @FXML
    public CheckBox chkbx1;
    @FXML
    public Button btn1;
    @FXML
    public Button btn2;
    @FXML
    public ComboBox<String> loc;
    @FXML
    public ComboBox<String> loc1;
    @FXML
    public TextField price;
    @FXML
    public TextField price1;
    @FXML
    public Text statusText;

    private GameManager gameManager;
    private MiscManager miscManager;
    private LocationManager locationManager;
    private boolean isAdmin;
    @FXML
    public void initialize(){
        gameManager = new GameManager();
        miscManager = new MiscManager();
        locationManager = new LocationManager();


        try {
            loadComboBoxData();
        } catch (SQLException e) {
            e.printStackTrace();
            statusText.setText("Failed to load data for ComboBoxes.");
        }

        btn.setOnAction(event -> addGame());
        btn1.setOnAction(event -> addMisc());
        btn2.setOnAction(event -> goBackToPreviousScreen());
    }

    private void loadComboBoxData() throws SQLException {
        List<String> releaseYears = gameManager.getAllReleaseYears();
        year.setItems(FXCollections.observableArrayList(releaseYears));
        year1.setItems(FXCollections.observableArrayList(releaseYears));

        List<String> publishers = gameManager.getAllPublishers();
        publi.setItems(FXCollections.observableArrayList(publishers));
        publi1.setItems(FXCollections.observableArrayList(publishers));


        List<String> genres = gameManager.getAllGenres();
        genre.setItems(FXCollections.observableArrayList(genres));
        genre1.setItems(FXCollections.observableArrayList(genres));


        List<String> consoles = gameManager.getAllConsoles();
        cons.setItems(FXCollections.observableArrayList(consoles));


        List<String> types = miscManager.getAllTypes();
        type.setItems(FXCollections.observableArrayList(types));


        List<String> locations = (List<String>) gameManager.getAllLocationNames();
        loc.setItems(FXCollections.observableArrayList(locations));
        loc1.setItems(FXCollections.observableArrayList(locations));

    }


    @FXML
    private void addGame() {
        String gameName = name.getText();
        String releaseYear = year.getValue();
        String publisher = publi.getValue();
        String genreValue = genre.getValue();
        String console = cons.getValue();
        String locationName = loc.getValue();
        double gamePrice;
        try {
            gamePrice = Double.parseDouble(price.getText());
        } catch (NumberFormatException e) {
            statusText.setText("Please enter a valid price.");
            return;
        }
        boolean isDigital = chkbx.isSelected();


        if (gameName.isEmpty() || releaseYear == null || publisher == null || genreValue == null || console == null || locationName == null) {
            statusText.setText("Please fill in all fields.");
            return;
        }

        Location location = locationManager.getLocation(locationName);
        Game newGame = new Game(0, gameName, gamePrice, isDigital, location, releaseYear, publisher, genreValue, console);

        boolean success = gameManager.addGame(newGame);

        if (success) {
            statusText.setText("Game added successfully.");
            clearFields();
        } else {
            statusText.setText("Failed to add the game.");
        }
    }
    private void clearFields() {
        name.clear();
        year.setValue(null);
        publi.setValue(null);
        genre.setValue(null);
        cons.setValue(null);
        loc.setValue(null);
        price.clear();
        chkbx.setSelected(false);
    }
    @FXML
    private void addMisc() {
        String miscName = name1.getText();
        String releaseYear = year1.getValue();
        String publisher = publi1.getValue();
        String genreValue = genre1.getValue();
        String typeValue = type.getValue();
        String locationName = loc1.getValue();
        double miscPrice;
        try {
            miscPrice = Double.parseDouble(price1.getText());
        } catch (NumberFormatException e) {
            statusText.setText("Please enter a valid price.");
            return;
        }
        boolean isDigital = chkbx1.isSelected();

        // Validate inputs
        if (miscName.isEmpty() || releaseYear == null || publisher == null || genreValue == null || typeValue == null || locationName == null) {
            statusText.setText("Please fill in all fields.");
            return;
        }

        Location location = locationManager.getLocation(locationName);
        Misc newMisc = new Misc(0, miscName, miscPrice, isDigital, location, releaseYear, publisher, genreValue, typeValue);

        boolean success = miscManager.addMisc(newMisc);

        if (success) {
            statusText.setText("Misc added successfully.");
            clearMiscFields();
        } else {
            statusText.setText("Failed to add the misc item.");
        }
    }

    private void clearMiscFields() {

            name1.clear();
            year1.setValue(null);
            publi1.setValue(null);
            genre1.setValue(null);
            type.setValue(null);
            loc1.setValue(null);
            price1.clear();
            chkbx1.setSelected(false);
        }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @FXML
    private void goBackToPreviousScreen() {
        try {
            if (isAdmin) {
                DB_ProjectMain.setScene("BeheerScherm3.fxml", 800, 800); // Admin screen
            } else {
                DB_ProjectMain.setScene("BeheerScherm4.fxml", 800, 800); // user screen
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the previous screen.", e);
        }
    }
}

