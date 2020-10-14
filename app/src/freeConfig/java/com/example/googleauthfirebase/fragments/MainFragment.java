package com.example.googleauthfirebase.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.googleauthfirebase.BuildConfig;
import com.example.googleauthfirebase.HttpWeather;
import com.example.googleauthfirebase.MainActivity;
import com.example.googleauthfirebase.R;
import com.example.googleauthfirebase.model.WeatherRequest;
import com.example.googleauthfirebase.util.ServerAPI;
import com.example.googleauthfirebase.util.ServerWeatherAPIGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private static ServerWeatherAPIGenerator serverWeatherAPIGenerator;

    Handler handler;

    EditText city;
    EditText temperature;
    EditText windSPD;
    EditText forecast;

    Button buttonRefresh;
    Button buttonHistory;
    String mCity;
    SharedPreferences sPref;

    private static final String TAG = "WEATHER";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        init(view);

        loadWeather();
        buttonSetClick();

        return view;
    }

    private void init(View view) {

        buttonRefresh = (Button) view.findViewById(R.id.buttonRefresh);
        buttonHistory = (Button) view.findViewById(R.id.buttonForecast5);

        city = view.findViewById(R.id.textCity);
        temperature = view.findViewById(R.id.textTemper);
        windSPD = view.findViewById(R.id.textWind);
        forecast = view.findViewById(R.id.textForecast);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            bundle.getString("city");
            mCity = bundle.getString("city");
        } else {
            mCity = loadPref(mCity);
        }

        handler = new Handler();
        HttpWeather httpClient = new HttpWeather();
    }

    private void buttonSetClick() {

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadWeather();
            }
        });


        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HistoryFragment historyFragment = new HistoryFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, historyFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();

                Bundle args = new Bundle();
                args.putString("city", mCity);
                historyFragment.setArguments(args);

            }
        });

    }

    private void loadWeather() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                ServerAPI serverAPI = ServerWeatherAPIGenerator.createServer();

                Call<WeatherRequest> weatherRequestRet = serverAPI.listWeather(mCity, BuildConfig.WEATHER_API_KEY);

                final Response<WeatherRequest> response;

                try {
                    response = weatherRequestRet.execute();
                    if (response.isSuccessful()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                displayWeather(response.body());
                            }
                        });
                    } else {
                        ServerWeatherAPIGenerator.parseError(response);
                        ((MainActivity) getActivity()).onClickDialogBuilder(getView());
                    }

                } catch (IOException e) {
                    Log.e(TAG, "Error load display");
                }
            }
        });
        thread.start();
    }

    private void displayWeather(WeatherRequest weatherRequest) {

        city.setText(weatherRequest.getName());
        temperature.setText(String.format(Locale.getDefault(), "%s °С", (int) weatherRequest.getMain().getTemp() - 273));
        windSPD.setText(String.format(Locale.getDefault(), "%s m/c", weatherRequest.getWind().getSpeed()));
        forecast.setText(weatherRequest.getWeather()[0].getDescription());
    }

    public String loadPref(String city) {
        sPref = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        String mCity = sPref.getString("city", "Moscow");
        return mCity;
    }
}