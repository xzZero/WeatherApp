package com.example.weatherapp;

public class Location {

    public Location(String time, String temp) {
        this.time = time;
        this.temp = temp;
    }

    private String time;
    private String temp;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }



}
