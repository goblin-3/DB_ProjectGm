package com.example.dbprojectgm.models;

public class Game extends Item{
    private int id;
    private String name;
    private double price;
    private String releaseYear;
    private String publisher;
    private String genre;
    private String console;

    public Game(int id, String name, double price,boolean isDigital,Location location,String releaseYear,String publisher , String genre , String console) {
        super(id, name, price, isDigital,location);
        this.releaseYear = releaseYear;
        this.publisher = publisher;
        this.genre =genre;
        this.console = console;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }
}
