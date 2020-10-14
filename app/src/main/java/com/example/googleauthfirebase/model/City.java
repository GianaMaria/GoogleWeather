package com.example.googleauthfirebase.model;

import android.text.format.DateFormat;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Locale;

@Entity(indices = {@Index(value = {"city", "date"})})
public class City {

    @ColumnInfo(name = "city")
    public String nameCity;

    @ColumnInfo(name = "date")
    public long date;

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String getNameCity() {
        return nameCity;
    }

    public long getDate() {
        return date;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDateFormat() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(this.date);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public City(String nameCity, long date) {
        this.nameCity = nameCity;
        this.date = date;
    }

    public City() {
    }
}