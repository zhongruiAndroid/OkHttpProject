package com.github.theokhttp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class TheOkHttpNetworkUtils {
    private static Context context;
    public static Context getContext() {
        return context;
    }
    public static void init(Context cxt) {
        context = cxt;
    }
    public static boolean isNetworkAvailable() {
        return isNetworkAvailable(context);
    }
    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        if(context==null){
            throw new IllegalArgumentException("context is null,please call TheOkHttpNetworkUtils.init(this) in application");
        }
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&activeNetwork.isConnectedOrConnecting();
    }

    public static boolean noNetwork() {
        return noNetwork(context);
    }
    public static boolean noNetwork(Context context) {
        return !isNetworkAvailable(context);
    }
}

