package com.example.googleauthfirebase.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.googleauthfirebase.BuildConfig;
import com.example.googleauthfirebase.R;
import com.example.googleauthfirebase.HttpWeather;
import com.example.googleauthfirebase.MainActivity;
import com.example.googleauthfirebase.adapters.WeatherStoryAdapter;
import com.example.googleauthfirebase.model.WeatherStory;
import com.example.googleauthfirebase.util.ServerAPI;
import com.example.googleauthfirebase.util.ServerWeatherAPIGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private static final String WEATHER_STORY_URL = "http://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s";
    private static final String TAG = "WEATHER";
    private HttpWeather httpClient;
    private static ServerWeatherAPIGenerator serverWeatherAPIGenerator;
    Handler handler;
    String mCity;
    TextView textViewHistoryCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        httpClient = new HttpWeather();
        handler = new Handler();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mCity = bundle.getString("city");
            loadWeatherStory(mCity);
        } else {
            Log.e("cityHistory", "Ошибка");
        }

        textViewHistoryCity = (TextView) view.findViewById(R.id.textViewHistoryCity);
        textViewHistoryCity.setText(mCity);

        return view;
    }

    private void loadWeatherStory(final String mCity) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                ServerAPI serverAPI = serverWeatherAPIGenerator.createServer();

                Call<WeatherStory> weatherStoryCall = serverAPI.listStory(mCity, BuildConfig.WEATHER_API_KEY);

                final Response<WeatherStory> response;

                try {
                    response = weatherStoryCall.execute();
                    if (response.isSuccessful()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                displayWeatherStory(response.body());
                            }
                        });
                    } else {
                        ServerWeatherAPIGenerator.parseError(response);
                        ((MainActivity) getActivity()).onClickDialogBuilder(getView());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error load display");

                }
            }
        });
        thread.start();
    }

    private void displayWeatherStory(WeatherStory weatherStory) {

        ArrayList<String[]> story = new ArrayList<>();
        String[] temp = new String[6];

        for (int i = 0; i < weatherStory.getList().length; i++) {
            temp[0] = String.valueOf(weatherStory.getList()[i].getDt());
            temp[1] = weatherStory.getList()[i].getMain().getTempCels() + " °С";
            temp[2] = weatherStory.getList()[i].getMain().getPressure() + " mmHg";
            temp[3] = weatherStory.getList()[i].getMain().getHumidity() + " %";
            temp[4] = String.valueOf(weatherStory.getList()[i].getWeather()[0].getMain());
            temp[5] = weatherStory.getList()[i].getWind().getSpeed() + " m/c";
            if (i % 8 == 1) {
                story.add(temp);
            }
            temp = new String[temp.length];
        }

        WeatherStoryAdapter adapter = new WeatherStoryAdapter(getContext(), story);
        ListView listViewStory = (ListView) getView().findViewById(R.id.listViewHistory);
        listViewStory.setAdapter(adapter);
    }
}