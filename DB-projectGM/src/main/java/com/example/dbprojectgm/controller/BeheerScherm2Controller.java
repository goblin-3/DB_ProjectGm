package com.example.dbprojectgm.controller;

import com.example.dbprojectgm.DB_ProjectMain;
import com.example.dbprojectgm.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class BeheerScherm2Controller  {
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
    public void initialize(){
        RegisterBtn.setOnAction(event -> {
            try {
                attemptRegister();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void attemptRegister() throws IOException {
        boolean register = registerUser(FNameFld.getText(),LNameFld.getText(),emailFld.getText(),PasswordFld.getText(),UsernameFld.getText(),FALSE,FALSE);
        if(register != TRUE){
            resultText.setText("something went wrong");
        } else{
            String resourceName ="home.fxml";
            DB_ProjectMain.setScene(resourceName,600,400);
        }
    }

    public boolean registerUser(String firstName, String lastName, String email, String password, String username, boolean isAdmin, boolean isEmployee) {
        String query = "INSERT INTO Customer (First_Name, Last_Name, Email, Password, Username, Is_Admin, Is_Employee) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, username);
            preparedStatement.setBoolean(6, isAdmin);
            preparedStatement.setBoolean(7, isEmployee);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
