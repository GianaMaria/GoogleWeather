package com.example.googleauthfirebase;

import android.os.Parcelable;

public class Parcel implements Parcelable {

    private String cityName;

    public Parcel(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeString(cityName);
    }

    public static final Creator<Parcel> CREATOR = new Creator<Parcel>() {
        @Override
        public Parcel createFromParcel(android.os.Parcel parcel) {
            String cityName = parcel.readString();
            return new Parcel(cityName);
        }

        @Override
        public Parcel[] newArray(int size) {
            return new Parcel[size];
        }
    };

}
