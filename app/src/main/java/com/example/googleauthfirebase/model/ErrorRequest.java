package com.example.googleauthfirebase.model;

public class ErrorRequest {
    private int cod;
    private String message;

    public int getCod() {
        return cod;
    }

    public String getMessage() {
        return message;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
