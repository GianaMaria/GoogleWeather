package com.example.googleauthfirebase.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.googleauthfirebase.R;
import com.example.googleauthfirebase.adapters.HistoryOfRequestsByCityAdapter;
import com.example.googleauthfirebase.model.City;
import com.example.googleauthfirebase.sqlite.App;
import com.example.googleauthfirebase.sqlite.RequestDao;

import java.util.ArrayList;
import java.util.List;

public class HistoryOfRequestsByCityFragment extends Fragment {

    ListView ListViewRequestDate;
    Button buttonClear;
    Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_history_of_requests_by_city, container, false);

        ListViewRequestDate = view.findViewById(R.id.ListViewRequestDate);
        buttonClear = view.findViewById(R.id.buttonClear);

        new Thread(new Runnable() {
            @Override
            public void run() {

                final RequestDao requestDao = App
                        .getInstance()
                        .getRequestDao();

                final List<City> listDao = requestDao.getAllSQLCity();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        displayHistory(listDao);
                    }
                });


            }
        }).start();

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final RequestDao requestDao = App
                                .getInstance()
                                .getRequestDao();

                        requestDao.clearTable();
                    }
                }).start();

            }
        });

        return view;
    }

    public void displayHistory(List<City> listDao) {
        HistoryOfRequestsByCityAdapter historyOfRequestsByCityAdapter =
                new HistoryOfRequestsByCityAdapter(getContext(), (ArrayList<City>) listDao);
        ListViewRequestDate.setAdapter(historyOfRequestsByCityAdapter);
    }

}