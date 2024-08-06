package com.example.dbprojectgm.controller;

import com.example.dbprojectgm.DB_ProjectMain;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class ProjectMainController extends MyController{
    @FXML
    private Button btnBeheerScherm1;
    @FXML
    private Button btnBeheerScherm2;
    //   @FXML
    //  private Button btnConfigAttaches;
    @FXML
    private TextField UsernameFld;

    @FXML
    private PasswordField PasswordFld;

    @FXML
    private Text statustxt;

    @FXML
    public void initialize(){
        btnBeheerScherm1.setOnAction(e -> {
            try {
                showBeheerscherm("1");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    private void attemptLogin() throws IOException {
        statustxt.setText("logging in...");
        showBeheerscherm("scherm4");
    }


    public void showBeheerscherm(String id) throws IOException {
        var resourceName = "beheer"+id+".fxml";
        MyController controller = null;
switch (id){
    case  "1": controller = new  BeheerScherm1Controller();
    case  "2": controller = new  BeheerScherm2Controller();
    case  "3": controller = new  BeheerScherm3Controller();
    case  "4": controller = new  BeheerScherm4Controller();
    case  "5": controller = new  BeheerScherm5Controller();
    case  "6": controller = new  BeheerScherm6Controller();
        }
        try {
            DB_ProjectMain.setScene(resourceName,800,800,controller);
        } catch (IOException e){
            throw new RuntimeException("failed to load stage "+ id,e);
        }

    }
}