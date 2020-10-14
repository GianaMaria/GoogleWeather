package com.example.googleauthfirebase.model;

public class WeatherStory {

    private int cod;
    private WeatherRequest[] list;

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public WeatherRequest[] getList() {
        return list;
    }

    public void setList(WeatherRequest[] list) {
        this.list = list;
    }

}
