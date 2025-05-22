package ru.example.model;

public class SportsObject {
    private int id;
    private String name;
    private String type;
    private String address;
    private String date;
    private String region;

    public SportsObject(int id, String name, String type, String address, String date, String region) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.address = address;
        this.date = date;
        this.region = region;
    }

    // Getters and setters

    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getAddress() { return address; }
    public String getDate() { return date; }
    public String getRegion() { return region; }
}