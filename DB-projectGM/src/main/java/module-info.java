module com.example.dbprojectgm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.dbprojectgm to javafx.fxml;
    exports com.example.dbprojectgm;
    exports com.example.dbprojectgm.controller;
    opens com.example.dbprojectgm.controller to javafx.fxml;
    exports com.example.dbprojectgm.models;
    opens com.example.dbprojectgm.models to javafx.fxml;
}