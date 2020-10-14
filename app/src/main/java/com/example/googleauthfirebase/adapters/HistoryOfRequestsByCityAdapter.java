package com.example.googleauthfirebase.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.googleauthfirebase.R;
import com.example.googleauthfirebase.model.City;

import java.util.ArrayList;

public class HistoryOfRequestsByCityAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int i) {
        return cities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View mView = view;
        if (mView == null) {
            mView = lInflater.inflate(R.layout.item_history_request_city, viewGroup, false);
        }

        City cityModel = getCity(i);
        cityModel.getDateFormat();

        ((TextView) mView.findViewById(R.id.request_city)).setText(cityModel.getNameCity());
        ((TextView) mView.findViewById(R.id.request_date)).setText(cityModel.getDateFormat());

        return mView;
    }

    City getCity(int i) {
        return ((City) getItem(i));
    }

    ArrayList<City> cities;
    Context ctx;
    LayoutInflater lInflater;

    public HistoryOfRequestsByCityAdapter(Context context, ArrayList<City> citiesRequest) {
        ctx = context;
        cities = citiesRequest;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
