package com.example.dbprojectgm.models;

import com.example.dbprojectgm.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MiscManager {
    public List<Misc> getAllMisc() {
        List<Misc> miscItems = new ArrayList<>();
        String query = "SELECT Misc.*, Releaseyear.Year, Genre.Genre_Name, Publisher.Publisher_Name, Type.Type_Name, " +
                "COALESCE(Museum.Name, Warehouse.Name) AS LocationName, " +
                "COALESCE(Museum.Cash_Reserve, Warehouse.Cash_Reserve) AS Cash_Reserve, " +
                "COALESCE(Museum.Location, Warehouse.Location) AS Adress " +
                "FROM Misc " +
                "JOIN Releaseyear ON Misc.Releaseyear_ID = Releaseyear.Releaseyear_ID " +
                "JOIN Genre ON Misc.Genre_ID = Genre.Genre_ID " +
                "JOIN Publisher ON Misc.Publisher_ID = Publisher.Publisher_ID " +
                "JOIN Type ON Misc.Type_ID = Type.Type_ID " +
                "LEFT JOIN Museum ON Misc.Misc_ID = Museum.Misc_ID " +
                "LEFT JOIN Warehouse ON Misc.Misc_ID = Warehouse.Misc_ID";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("Misc_ID");
                String name = resultSet.getString("Misc_Name");
                double price = resultSet.getDouble("Price");
                boolean isDigital = resultSet.getBoolean("Is_Digital");
                String releaseYear = resultSet.getString("Year");
                String genre = resultSet.getString("Genre_Name");
                String publisher = resultSet.getString("Publisher_Name");
                String type = resultSet.getString("Type_Name");
                String locationName = resultSet.getString("LocationName");
                double cashReserve = resultSet.getDouble("Cash_Reserve");
                String address = resultSet.getString("Adress");

                Location location = getLocation(locationName, cashReserve, address, conn, id);

                miscItems.add(new Misc(id, name, price, isDigital, location, releaseYear, publisher, genre, type));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return miscItems;
    }

    public boolean addMisc(Misc misc) {
        String query = "INSERT INTO Misc (Misc_Name, Price, Is_Digital, Releaseyear_ID, Genre_ID, Publisher_ID, Type_ID, Location) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, misc.getName());
            pstmt.setDouble(2, misc.getPrice());
            pstmt.setBoolean(3, misc.isDigital());
            pstmt.setInt(4, getReleaseyearId(misc.getReleaseYear(), conn));
            pstmt.setInt(5, getGenreId(misc.getGenre(), conn));
            pstmt.setInt(6, getPublisherId(misc.getPublisher(), conn));
            pstmt.setInt(7, getTypeId(misc.getType(), conn));
            pstmt.setString(8, misc.getLocation().getName());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteMiscById(int miscId) {
        String query = "DELETE FROM Misc WHERE Misc_ID = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, miscId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    private Location getLocation(String name, double cashReserve, String address, Connection conn, int gameId) throws SQLException{

        String queryMuseum = "SELECT * FROM Museum WHERE Game_ID = ?";
        String queryWarehouse = "SELECT * FROM Warehouse WHERE Game_ID = ?";

        try (PreparedStatement preparedStatementMuseum = conn.prepareStatement(queryMuseum);
             PreparedStatement preparedStatementWarehouse = conn.prepareStatement(queryWarehouse)) {


            preparedStatementMuseum.setInt(1, gameId);
            ResultSet rsMuseum = preparedStatementMuseum.executeQuery();
            if (rsMuseum.next()) {
                return new Museum(name, cashReserve, address);
            }


            preparedStatementWarehouse.setInt(1, gameId);
            ResultSet rsWarehouse = preparedStatementWarehouse.executeQuery();
            if (rsWarehouse.next()) {
                return new Warehouse(name, cashReserve, address);
            }
        }
        return null;
    }



    private int getTypeId(String typeName, Connection conn) throws SQLException {
        String query = "SELECT Type_ID FROM Type WHERE Type_Name = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, typeName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Type_ID");
            } else {
                throw new SQLException("Type not found");
            }
        }
    }
    private int getReleaseyearId(String releaseYear, Connection conn) throws SQLException {
        String query = "SELECT Releaseyear_ID FROM Releaseyear WHERE Year = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, releaseYear);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Releaseyear_ID");
            } else {
                throw new SQLException("Release year not found");
            }
        }
    }

    private int getPublisherId(String publisherName, Connection conn) throws SQLException {
        String query = "SELECT Publisher_ID FROM Publisher WHERE Publisher_Name = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, publisherName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Publisher_ID");
            } else {
                throw new SQLException("Publisher not found");
            }
        }
    }

    private int getGenreId(String genreName, Connection conn) throws SQLException {
        String query = "SELECT Genre_ID FROM Genre WHERE Genre_Name = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, genreName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Genre_ID");
            } else {
                throw new SQLException("Genre not found");
            }
        }
    }

    public List<Misc> getMiscFromLocation(Location location) {
        List<Misc> miscList = new ArrayList<>();
        String query = "SELECT Misc.*, Releaseyear.Year, Publisher.Publisher_Name, Genre.Genre_Name, Type.Type_Name, " +
                "COALESCE(Museum.Name, Warehouse.Name) AS LocationName, " +
                "COALESCE(Museum.Cash_Reserve, Warehouse.Cash_Reserve) AS Cash_Reserve, " +
                "COALESCE(Museum.Location, Warehouse.Location) AS Adress " +
                "FROM Misc " +
                "JOIN Releaseyear ON Misc.Releaseyear_ID = Releaseyear.Releaseyear_ID " +
                "JOIN Publisher ON Misc.Publisher_ID = Publisher.Publisher_ID " +
                "JOIN Genre ON Misc.Genre_ID = Genre.Genre_ID " +
                "JOIN Type ON Misc.Type_ID = Type.Type_ID " +
                "LEFT JOIN Museum ON Misc.Misc_ID = Museum.Misc_ID " +
                "LEFT JOIN Warehouse ON Misc.Misc_ID = Warehouse.Misc_ID " +
                "WHERE COALESCE(Museum.Name, Warehouse.Name) = ? " +
                "GROUP BY Misc.Misc_ID";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, location.getName());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("Misc_ID");
                    String name = resultSet.getString("Misc_Name");
                    double price = resultSet.getDouble("Price");
                    boolean isDigital = resultSet.getBoolean("Is_Digital");
                    String releaseYear = resultSet.getString("Year");
                    String publisher = resultSet.getString("Publisher_Name");
                    String genre = resultSet.getString("Genre_Name");
                    String type = resultSet.getString("Type_Name");

                    miscList.add(new Misc(id, name, price, isDigital, location, releaseYear, publisher, genre, type));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return miscList;
    }


    public List<String> getAllTypes() {
        List<String> types = new ArrayList<>();
        String query = "SELECT Type_Name FROM Type";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                types.add(resultSet.getString("Type_Name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return types;
    }



}
