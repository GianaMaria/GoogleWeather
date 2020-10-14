package com.example.googleauthfirebase.sqlite;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {

    private static App instance;

    //База данных
    private SQLWeatherDatabase db;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Сохраняем объект приложения (для Singleton’а)
        instance = this;

        // Строим базу
        db = Room.databaseBuilder(
                getApplicationContext(),
                SQLWeatherDatabase.class,
                "sqlweather_database")
                .build();

    }

    // Получаем Dao для составления запросов
    public RequestDao getRequestDao() {
        return db.getRequestDao();
    }

}
