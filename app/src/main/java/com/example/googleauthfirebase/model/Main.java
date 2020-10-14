package com.example.googleauthfirebase.model;

public class Main {
    private float temp;
    private int pressure;
    private int humidity;

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public int getPressure() {
        return (int) pressure;
    }

    public int getHumidity() {
        return (int) humidity;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getTempCels() {
        return (int) temp - 273;
    }
}
