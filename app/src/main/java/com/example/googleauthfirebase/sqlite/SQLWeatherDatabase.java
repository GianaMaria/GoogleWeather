package com.example.googleauthfirebase.sqlite;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.googleauthfirebase.model.City;


@Database(entities = {City.class}, version = 1)
public abstract class SQLWeatherDatabase extends RoomDatabase {

    public abstract RequestDao getRequestDao();
}
