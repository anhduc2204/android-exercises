package com.deso1.daoanhduc.dlu_21a100100093.model;

public class Food {
    private int id;
    private String name;
    private double price;
    private String unit;
    private String imagePath;

    public Food(int id, String name, double price, String unit, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getUnit() { return unit; }
    public String getImagePath() { return imagePath; }
}

