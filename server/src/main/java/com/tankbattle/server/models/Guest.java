package com.tankbattle.server.models;

public class Guest {
    private String name;
    public Guest(){}
    public Guest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
