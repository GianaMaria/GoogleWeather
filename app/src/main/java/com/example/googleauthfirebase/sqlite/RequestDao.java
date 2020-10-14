package com.example.googleauthfirebase.sqlite;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.googleauthfirebase.model.City;

import java.util.List;

@Dao
public interface RequestDao {

    // добавление, в случае конфликта - замена
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSQLCity(City sqlCity);

    // замена данных
    @Update
    void updateSQLCity(City sqlCity);

    // удаление
    @Delete
    void deleteSQLCity(City sqlCity);

    // удаление по ключу (айди)
    @Query("DELETE FROM City WHERE id = :id")
    void deleteSQLCityById(long id);

    // получение данных по всей таблице
    @Query("SELECT * FROM City ORDER BY date DESC")
    List<City> getAllSQLCity();

    // получение данных по айди
    @Query("SELECT * FROM City WHERE id = :id")
    City getSQLCityById(long id);

    // получение количества записей в таблице
    @Query("SELECT COUNT() FROM City")
    long getCountSQLCity();

    @Query("DELETE FROM City")
    public void clearTable();

}