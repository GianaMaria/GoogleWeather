package com.example.googleauthfirebase;

import com.example.googleauthfirebase.model.WeatherRequest;
import com.example.googleauthfirebase.model.WeatherStory;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class HttpWeather {

    private Gson gson = new Gson();
    private static final String TAG = "WEATHER";

    public WeatherRequest getWeather(String requestUrl) throws IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET"); // установка метода получения данных -GET
        connection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
        InputStream in;

        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            in = connection.getErrorStream();
        } else {
            in = connection.getInputStream();
        }

        return convertStream(in);
    }

    private WeatherRequest convertStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String result = getLines(reader);
        stream.close();
        return gson.fromJson(result, WeatherRequest.class);
    }

    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    public WeatherStory getWeatherStory(String requestUrl) throws IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET"); // установка метода получения данных -GET
        connection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
        InputStream in;
        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            in = connection.getErrorStream();
        } else {
            in = connection.getInputStream();
        }

        return convertStreamStory(in);
    }

    private WeatherStory convertStreamStory(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String result = getLines(reader);
        stream.close();
        return gson.fromJson(result, WeatherStory.class);
    }
}
