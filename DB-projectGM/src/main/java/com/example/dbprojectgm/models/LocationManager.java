package com.example.dbprojectgm.models;

import com.example.dbprojectgm.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationManager {

    public boolean createLocation(boolean isWarehouse, Location location) {
        String query;

        if (isWarehouse) {
            query = "INSERT INTO Warehouse (Name, Cash_Reserve, Location) VALUES (?, ?, ?)";
        } else {
            query = "INSERT INTO Museum (Name, Cash_Reserve, Location) VALUES (?, ?, ?)";
        }

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, location.getName());
            preparedStatement.setDouble(2, location.getCash_reserve());
            preparedStatement.setString(3, location.getAdress());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean deleteLocation(Location location) {
        String checkGamesQuery;
        String checkMiscQuery;
        String deleteLocationQuery;

        if (location instanceof Warehouse) {
            checkGamesQuery = "SELECT COUNT(*) FROM Game WHERE Game_ID IN (SELECT Game_ID FROM Warehouse WHERE Name = ?)";
            checkMiscQuery = "SELECT COUNT(*) FROM Misc WHERE Misc_ID IN (SELECT Misc_ID FROM Warehouse WHERE Name = ?)";
            deleteLocationQuery = "DELETE FROM Warehouse WHERE Name = ?";
        } else if (location instanceof Museum) {
            checkGamesQuery = "SELECT COUNT(*) FROM Game WHERE Game_ID IN (SELECT Game_ID FROM Museum WHERE Name = ?)";
            checkMiscQuery = "SELECT COUNT(*) FROM Misc WHERE Misc_ID IN (SELECT Misc_ID FROM Museum WHERE Name = ?)";
            deleteLocationQuery = "DELETE FROM Museum WHERE Name = ?";
        } else {
            return false;
        }

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement checkGamesStmt = conn.prepareStatement(checkGamesQuery);
             PreparedStatement checkMiscStmt = conn.prepareStatement(checkMiscQuery);
             PreparedStatement deleteLocationStmt = conn.prepareStatement(deleteLocationQuery)) {


            checkGamesStmt.setString(1, location.getName());
            ResultSet gamesResult = checkGamesStmt.executeQuery();
            if (gamesResult.next() && gamesResult.getInt(1) > 0) {
                return false;
            }


            checkMiscStmt.setString(1, location.getName());
            ResultSet miscResult = checkMiscStmt.executeQuery();
            if (miscResult.next() && miscResult.getInt(1) > 0) {
                return false;
            }


            deleteLocationStmt.setString(1, location.getName());
            int rowsAffected = deleteLocationStmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Location getLocation(String locationName) {
        String queryWarehouse = "SELECT * FROM Warehouse WHERE Name = ?";
        String queryMuseum = "SELECT * FROM Museum WHERE Name = ?";

        try (Connection conn = DatabaseManager.getConnection()) {

            try (PreparedStatement preparedStatementWarehouse = conn.prepareStatement(queryWarehouse)) {
                preparedStatementWarehouse.setString(1, locationName);
                try (ResultSet resultSetWarehouse = preparedStatementWarehouse.executeQuery()) {
                    if (resultSetWarehouse.next()) {
                        double cashReserve = resultSetWarehouse.getDouble("Cash_Reserve");
                        String address = resultSetWarehouse.getString("Location");
                        return new Warehouse(locationName, cashReserve, address);
                    }
                }
            }


            try (PreparedStatement preparedStatementMuseum = conn.prepareStatement(queryMuseum)) {
                preparedStatementMuseum.setString(1, locationName);
                try (ResultSet resultSetMuseum = preparedStatementMuseum.executeQuery()) {
                    if (resultSetMuseum.next()) {
                        double cashReserve = resultSetMuseum.getDouble("Cash_Reserve");
                        String address = resultSetMuseum.getString("Location");
                        return new Museum(locationName, cashReserve, address);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



}
