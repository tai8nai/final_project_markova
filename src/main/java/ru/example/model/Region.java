package ru.example.model;

public class Region {
    private String name;
    private int objectCount;

    public Region(String name, int objectCount) {
        this.name = name;
        this.objectCount = objectCount;
    }

    public String getName() { return name; }
    public int getObjectCount() { return objectCount; }
    public void setObjectCount(int objectCount) { this.objectCount = objectCount; }
}