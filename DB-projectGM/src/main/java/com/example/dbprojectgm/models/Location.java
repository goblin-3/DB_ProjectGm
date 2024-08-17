package com.example.dbprojectgm.models;

public abstract class Location {
    private String name;
    private double Cash_reserve;
    private String adress;

    public Location(String name, double cash_reserve, String adress) {
        this.name = name;
        Cash_reserve = cash_reserve;
        this.adress = adress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCash_reserve() {
        return Cash_reserve;
    }

    public void setCash_reserve(double cash_reserve) {
        Cash_reserve = cash_reserve;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
