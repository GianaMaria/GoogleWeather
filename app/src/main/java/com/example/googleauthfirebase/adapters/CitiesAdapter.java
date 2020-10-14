package com.example.googleauthfirebase.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleauthfirebase.R;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder> {

    private String[] cities;
    private OnCityClickListener onCityClickListener;

    public void setOnCityClickListener(OnCityClickListener onCityClickListener) {
        this.onCityClickListener = onCityClickListener;
    }

    public void setCities(String[] cities) {
        this.cities = cities;
        notifyDataSetChanged();
    }

    public CitiesAdapter(String[] cities) {
        this.cities = cities;
    }

    public String[] getCities() {
        return cities;
    }


    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CityViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cities, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        holder.bind(cities[position], onCityClickListener);
    }

    @Override
    public int getItemCount() {
        if (cities == null) {
            return 0;
        } else {
            return cities.length;
        }
    }

    protected class CityViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final View chevron;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewCity);
            chevron = itemView.findViewById(R.id.imageView);
        }

        void bind(final String city, final OnCityClickListener onCityClickListener) {
            textView.setText(city);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onCityClickListener != null) {
                        onCityClickListener.onClicked(city);
                    }

                }
            });
        }
    }

    public interface OnCityClickListener {
        void onClicked(String city);
    }
}
