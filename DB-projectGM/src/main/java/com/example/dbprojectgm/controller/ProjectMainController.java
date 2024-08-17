package com.example.dbprojectgm.controller;

import com.example.dbprojectgm.DB_ProjectMain;
import com.example.dbprojectgm.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.*;

import static java.lang.Boolean.TRUE;

public class ProjectMainController  {
    @FXML
    private Button btnBeheerScherm1;
    @FXML
    private Button btnBeheerScherm2;
    @FXML
    private TextField UsernameFld;

    @FXML
    private PasswordField PasswordFld;

    @FXML
    private Text statustxt;

    public String username;

    public boolean isAdmin;

    @FXML
    public void initialize(){
        btnBeheerScherm1.setOnAction(e -> {
            try {
                attemptLogin();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnBeheerScherm2.setOnAction(event -> {
            try {
                showBeheerscherm("2");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void attemptLogin() throws IOException {
        statustxt.setText("logging in...");
        username = UsernameFld.toString();
        String password = PasswordFld.toString();
        String query = "SELECT Password, Is_Admin FROM Customer WHERE Username = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(query)){
            prepareStatement.setString(1,username);
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()&&resultSet.getString("Password").equals( password)) {

                if (resultSet.getInt("Is_Admin")==0){
                showBeheerscherm("4");
                }else{

                    showBeheerscherm("3");
                }
            }else{
                statustxt.setText("log in failed");
            }

        } catch (SQLException e){
            e.printStackTrace();
        }


    }


    public void showBeheerscherm(String id) throws IOException {
        var resourceName = "beheerscherm"+id+".fxml";
        try {
            DB_ProjectMain.setScene(resourceName,800,800);
        } catch (IOException e){
            throw new RuntimeException("failed to load stage :"+ id,e);
        }

    }
}