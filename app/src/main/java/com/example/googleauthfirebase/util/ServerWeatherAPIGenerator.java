package com.example.googleauthfirebase.util;

import android.util.Log;

import com.example.googleauthfirebase.model.ErrorRequest;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerWeatherAPIGenerator {

    private static String url = "http://api.openweathermap.org/data/2.5/";

    private static final String TAG = "error";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ServerAPI createServer() {

        ServerAPI serverAPI = retrofit.create(ServerAPI.class);
        return serverAPI;
    }

    public static ErrorRequest parseError(Response<?> response) {
        Converter<ResponseBody, ErrorRequest> converter =
                retrofit.responseBodyConverter(ErrorRequest.class, new Annotation[0]);

        ErrorRequest error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            error = new ErrorRequest();
            Log.e(TAG, "error convert");
        }
        return error;
    }
}
