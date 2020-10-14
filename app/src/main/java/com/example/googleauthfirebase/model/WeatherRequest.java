package com.example.googleauthfirebase.model;

public class WeatherRequest {
    private Coord coord;
    private Weather[] weather;
    private Main main;
    private Wind wind;
    private String name;
    private int cod;
    private int dt;

    public Coord getCoord() {
        return coord;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public String getName() {
        return name;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }


    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }
}
