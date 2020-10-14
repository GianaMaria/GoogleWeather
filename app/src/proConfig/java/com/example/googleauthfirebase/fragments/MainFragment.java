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
    private LocationListener locationListener;
    private static final int PERMISSION_REQUEST_CODE = 10;
    private String TAG = "Geolocation";
    private LocationManager locationManager;

    Handler handler;

    EditText city;
    EditText temperature;
    EditText windSPD;
    EditText forecast;

    Button buttonRefresh;
    Button buttonHistory;
    Button buttonLocation;
    String mCity;
    SharedPreferences sPref;

    private static final String TAG1 = "WEATHER";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        init(view);

        loadWeather();
        buttonSetClick();

        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);

        return view;
    }

    private void init(View view) {

        buttonRefresh = (Button) view.findViewById(R.id.buttonRefresh);
        buttonHistory = (Button) view.findViewById(R.id.buttonForecast5);
        buttonLocation = (Button) view.findViewById(R.id.buttonLocation);

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

        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions();
                Toast.makeText(getActivity(),"wait, loading", Toast.LENGTH_SHORT).show();
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

    private void loadWeatherCord(final String lat, final String lon) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                ServerAPI serverAPI = ServerWeatherAPIGenerator.createServer();

                Call<WeatherRequest> weatherRequestRet = serverAPI.listWeatherLat(lat, lon, BuildConfig.WEATHER_API_KEY);

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

    // Запрашиваем координаты
    public List<String> requestLocation() {
        final List<String> geo = new ArrayList<>();
        // Если Permission’а всё- таки нет, просто выходим: приложение не имеет
        // смысла
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            geo.add("error");
        // Получаем менеджер геолокаций
        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            // Будем получать геоположение через каждые 10 секунд или каждые
            // 10 метров
            locationManager.requestLocationUpdates(provider, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    setCordCityName(location);
                    loadWeather();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
        return geo;
    }


    private void requestPermissions() {
        // Проверим, есть ли Permission’ы, и если их нет, запрашиваем их у
        // пользователя
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Запрашиваем координаты
            requestLocation();
        } else {
            // Permission’ов нет, запрашиваем их у пользователя
            requestLocationPermissions();
        }
    }

    // Запрашиваем Permission’ы для геолокации
    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
            // Запрашиваем эти два Permission’а у пользователя
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    // Результат запроса Permission’а у пользователя:
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {   // Запрошенный нами
            // Permission
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                // Все препоны пройдены и пермиссия дана
                // Запросим координаты
                requestLocation();
            }
        }
    }

    public void setCordCityName(Location location) {
        double lat = location.getLatitude(); // Широта
        String latitude = Double.toString(lat);

        double lng = location.getLongitude(); // Долгота
        String longitude = Double.toString(lng);

        Log.d("locationLogD", latitude);
        Log.d("locationLogD", longitude);

        String cityName = null;
        Geocoder gcd = new Geocoder(getContext(), Locale.ENGLISH);
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(lat,
                    lng, 1);
            if (addresses != null && addresses.size() > 0) {
                Log.d("eeeee", addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        mCity = cityName;
    }
}