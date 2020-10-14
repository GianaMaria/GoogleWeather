package com.example.googleauthfirebase.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleauthfirebase.R;
import com.example.googleauthfirebase.MainActivity;
import com.example.googleauthfirebase.adapters.CitiesAdapter;
import com.example.googleauthfirebase.model.City;
import com.example.googleauthfirebase.sqlite.App;
import com.example.googleauthfirebase.sqlite.RequestDao;

import java.util.Date;

public class OtherCitiesFragment extends Fragment {

    private String[] cities;

    SharedPreferences sPref;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_other_cities, container, false);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        cities = getResources().getStringArray(R.array.cities);
        final CitiesAdapter adapter = new CitiesAdapter(cities);

        recyclerView.setAdapter(adapter);

        final Date date = new Date();

        adapter.setOnCityClickListener(new CitiesAdapter.OnCityClickListener() {
            @Override
            public void onClicked(final String city) {
                int fi = 0;
                for (int i = 0; i < cities.length; i++) {
                    if (city.equals(cities[i])) {
                        fi = i;
                    }
                }

                MainFragment mainCityFragment = new MainFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, mainCityFragment);  // замена фрагмента
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();

                Bundle args = new Bundle();
                args.putString("city", getResources().getStringArray(R.array.cities)[fi]);
                mainCityFragment.setArguments(args);

                ((MainActivity) getActivity()).addCityArray(city, date.getTime());

                savePref(city);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        RequestDao requestDao = App
                                .getInstance()
                                .getRequestDao();

                        City sqlCity = new City();
                        sqlCity.nameCity = city;
                        sqlCity.date = date.getTime();

                        requestDao.insertSQLCity(sqlCity);

                    }
                }).start();
            }
        });

        return view;
    }

    public void savePref(String city) {
        sPref = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString("city", city);
        editor.apply();
    }

}