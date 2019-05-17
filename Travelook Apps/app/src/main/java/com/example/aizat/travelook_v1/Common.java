package com.example.aizat.travelook_v1;

import com.example.aizat.travelook_v1.PlacesModel.Results;
import com.example.aizat.travelook_v1.Remote.IGoogleAPIService;
import com.example.aizat.travelook_v1.Remote.RetrofitClient;
import com.example.aizat.travelook_v1.Remote.RetrofitScalarsClient;

import retrofit2.Retrofit;

public class Common {

    public static Results currentResult;
    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";
    public static IGoogleAPIService getGoogleAPIService()
    {
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
    public static IGoogleAPIService getGoogleAPIServiceScalars()
    {
        return RetrofitScalarsClient.getScalarClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
