package com.example.dbprojectgm.models;

public abstract class Item {
    private int id;
    private String name;
    private double price;

    private boolean isDigital;

    private Location location;


    public Item(int id, String name, double price,boolean isDigital,Location location) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isDigital = isDigital;
        this.location=location;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isDigital() {
        return isDigital;
    }

    public void setDigital(boolean digital) {
        isDigital = digital;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
