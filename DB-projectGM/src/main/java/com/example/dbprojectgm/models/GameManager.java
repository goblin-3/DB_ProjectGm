package com.example.dbprojectgm.models;

import com.example.dbprojectgm.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameManager {
    public List<Game> getAllGames(){
        List<Game> gamesList = new ArrayList<>();
        String query = "SELECT Game.*, Releaseyear.Year, Publisher.Publisher_Name, Console.Console_Name, " +
                "GROUP_CONCAT(Genre.Genre_Name, ', ') AS Genres, " +
                "COALESCE(Museum.Name, Warehouse.Name) AS LocationName, " +
                "COALESCE(Museum.Cash_Reserve, Warehouse.Cash_Reserve) AS Cash_Reserve, " +
                "COALESCE(Museum.Location, Warehouse.Location) AS Adress " +
                "FROM Game " +
                "JOIN Releaseyear ON Game.Releaseyear_ID = Releaseyear.Releaseyear_ID " +
                "JOIN Publisher ON Game.Publisher_ID = Publisher.Publisher_ID " +
                "JOIN Console ON Game.Console_ID = Console.Console_ID " +
                "LEFT JOIN Game_Genre ON Game.Game_ID = Game_Genre.Game_ID " +
                "LEFT JOIN Genre ON Game_Genre.Genre_ID = Genre.Genre_ID " +
                "LEFT JOIN Museum ON Game.Game_ID = Museum.Game_ID " +
                "LEFT JOIN Warehouse ON Game.Game_ID = Warehouse.Game_ID " +
                "GROUP BY Game.Game_ID";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()){

            while (resultSet.next()){
                int id = resultSet.getInt("Game_ID");
                String name = resultSet.getString("Game_Name");
                double price = resultSet.getDouble("Price");
                boolean isDigital = resultSet.getBoolean("Is_Digital");
                String releaseYear = resultSet.getString("Year");
                String publisher = resultSet.getString("Publisher_Name");
                String console = resultSet.getString("Console_Name");
                String genre = resultSet.getString("Genres");
                String locationName = resultSet.getString("LocationName");
                double cashReserve = resultSet.getDouble("Cash_Reserve");
                String address = resultSet.getString("Adress");

                Location location = getLocation(locationName,cashReserve,address,conn,id);


                gamesList.add(new Game(id, name, price, isDigital, location, releaseYear, publisher, genre, console));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return gamesList;
    }

    public boolean deleteGameById(int gameId) {
        String query = "DELETE FROM Game WHERE Game_ID = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, gameId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean addGame(Game game) {
        String insertGameQuery = "INSERT INTO Game (Game_Name, Price, Is_Digital, Releaseyear_ID, Publisher_ID, Console_ID, Location) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertGameGenreQuery = "INSERT INTO Game_Genre (Game_ID, Genre_ID) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement gameStatement = conn.prepareStatement(insertGameQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement genreStatement = conn.prepareStatement(insertGameGenreQuery)) {

            gameStatement.setString(1, game.getName());
            gameStatement.setDouble(2, game.getPrice());
            gameStatement.setBoolean(3, game.isDigital());
            gameStatement.setInt(4, getReleaseyearId(game.getReleaseYear(), conn));
            gameStatement.setInt(5, getPublisherId(game.getPublisher(), conn));
            gameStatement.setInt(6, getConsoleId(game.getConsole(), conn));
            gameStatement.setString(7, game.getLocation().getName());

            int rowsAffected = gameStatement.executeUpdate();

            if (rowsAffected > 0) {

                try (ResultSet generatedKeys = gameStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int gameId = generatedKeys.getInt(1);
                        String[] genres = game.getGenre().split(", ");
                        for (String genre : genres) {
                            int genreId = getGenreId(genre, conn);
                            genreStatement.setInt(1, gameId);
                            genreStatement.setInt(2, genreId);
                            genreStatement.executeUpdate();
                        }
                    } else {
                        throw new SQLException("Creating game failed, no ID obtained.");
                    }
                }
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int getConsoleId(String console, Connection conn) throws SQLException {
        String query = "SELECT Console_ID FROM Console WHERE Console_Name = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)){
            preparedStatement.setString(1,console);
            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()){
                return resultSet.getInt("Console_ID");
            } else {
                throw new SQLException("console not found "+ console);
            }
        }

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

    public List<Game> getGamesFromLocation(Location location){
        List<Game> gamesList = new ArrayList<>();
        String query = "SELECT Game.*, Releaseyear.Year, Publisher.Publisher_Name, Console.Console_Name, " +
                "GROUP_CONCAT(Genre.Genre_Name, ', ') AS Genres, " +
                "COALESCE(Museum.Name, Warehouse.Name) AS LocationName, " +
                "COALESCE(Museum.Cash_Reserve, Warehouse.Cash_Reserve) AS Cash_Reserve, " +
                "COALESCE(Museum.Location, Warehouse.Location) AS Adress " +
                "FROM Game " +
                "JOIN Releaseyear ON Game.Releaseyear_ID = Releaseyear.Releaseyear_ID " +
                "JOIN Publisher ON Game.Publisher_ID = Publisher.Publisher_ID " +
                "JOIN Console ON Game.Console_ID = Console.Console_ID " +
                "LEFT JOIN Game_Genre ON Game.Game_ID = Game_Genre.Game_ID " +
                "LEFT JOIN Genre ON Game_Genre.Genre_ID = Genre.Genre_ID " +
                "LEFT JOIN Museum ON Game.Game_ID = Museum.Game_ID " +
                "LEFT JOIN Warehouse ON Game.Game_ID = Warehouse.Game_ID " +
                "WHERE COALESCE(Museum.Name, Warehouse.Name) = ? " +
                "GROUP BY Game.Game_ID";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()){

            while (resultSet.next()){
                int id = resultSet.getInt("Game_ID");
                String name = resultSet.getString("Game_Name");
                double price = resultSet.getDouble("Price");
                boolean isDigital = resultSet.getBoolean("Is_Digital");
                String releaseYear = resultSet.getString("Year");
                String publisher = resultSet.getString("Publisher_Name");
                String console = resultSet.getString("Console_Name");
                String genre = resultSet.getString("Genres");

                gamesList.add(new Game(id, name, price, isDigital, location, releaseYear, publisher, genre, console));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return gamesList;
    }

    public Collection<String> getAllLocationNames() throws SQLException {
        Collection<String> locationNames = new ArrayList<>();

        String query = "SELECT DISTINCT Name FROM (" +
                "SELECT Name FROM Warehouse " +
                "UNION " +
                "SELECT Name FROM Museum)";
        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()){
                locationNames.add(resultSet.getString("Name"));
            }


        } catch (SQLException e){
            e.printStackTrace();
        }
        return locationNames;

    }

    public List<String> getAllReleaseYears() {
        List<String> releaseYears = new ArrayList<>();
        String query = "SELECT Year FROM Releaseyear";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                releaseYears.add(resultSet.getString("Year"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return releaseYears;
    }
    public List<String> getAllPublishers() {
        List<String> publishers = new ArrayList<>();
        String query = "SELECT Publisher_Name FROM Publisher";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                publishers.add(resultSet.getString("Publisher_Name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publishers;
    }
    public List<String> getAllGenres() {
        List<String> genres = new ArrayList<>();
        String query = "SELECT Genre_Name FROM Genre";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                genres.add(resultSet.getString("Genre_Name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return genres;
    }

    public List<String> getAllConsoles() {
        List<String> consoles = new ArrayList<>();
        String query = "SELECT Console_Name FROM Console";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                consoles.add(resultSet.getString("Console_Name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consoles;
    }


}
