package com.shu.bakingtime.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class BakingTimeUtils {

    private static final String BAKING_TIME_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    public static BakingTimeRecipes getBakingTimeService(){
        return com.shu.bakingtime.utils.RetrofitClient.getInstance(BAKING_TIME_URL).create(BakingTimeRecipes.class);
    }

    public static boolean isOnline(Context context){
        ConnectivityManager c =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (c == null)
            return false;

        NetworkInfo info = c.getActiveNetworkInfo();

        return (info != null
                && info.isConnectedOrConnecting()
                && info.isConnected());
    }
}
