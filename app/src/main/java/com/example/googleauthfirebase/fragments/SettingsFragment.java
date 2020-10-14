package com.example.googleauthfirebase.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.googleauthfirebase.R;

public class SettingsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button buttonSave = (Button) view.findViewById(R.id.buttonSaveSettings);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment mainCityFragment = new MainFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, mainCityFragment)
                        .commit();
            }
        });

        return view;
    }
}