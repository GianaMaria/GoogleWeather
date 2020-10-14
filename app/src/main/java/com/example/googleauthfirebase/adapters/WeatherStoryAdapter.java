package com.example.googleauthfirebase.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.googleauthfirebase.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class WeatherStoryAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return mWeatherStory.size();
    }

    @Override
    public Object getItem(int i) {
        return mWeatherStory.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View mView = view;
        if (mView == null) {
            mView = lInflater.inflate(R.layout.item_list_history, viewGroup, false);
        }

        String[] s = getWeatherStory(i);

        ((TextView) mView.findViewById(R.id.date_history)).setText(getDate(Long.parseLong(s[0])));
        ((TextView) mView.findViewById(R.id.temperature_history)).setText(s[1]);
        ((TextView) mView.findViewById(R.id.pressure_history)).setText(s[2]);
        ((TextView) mView.findViewById(R.id.Humidity_history)).setText(s[3]);
        ((TextView) mView.findViewById(R.id.forecast_history)).setText(s[4]);
        ((TextView) mView.findViewById(R.id.windspd_history)).setText(s[5]);

        return mView;
    }

    ArrayList<String[]> mWeatherStory;
    Context ctx;
    LayoutInflater lInflater;

    public WeatherStoryAdapter(Context context, ArrayList<String[]> weatherStory) {
        ctx = context;
        mWeatherStory = weatherStory;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    String[] getWeatherStory(int i) {
        return ((String[]) getItem(i));
    }

    //convert date
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }
}
