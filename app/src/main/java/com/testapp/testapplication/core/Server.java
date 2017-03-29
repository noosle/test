package com.testapp.testapplication.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by noosle on 29.03.2017.
 */

public class Server {

    public static String MAIN_DOMAIN_ADDRESS = "http://jsonplaceholder.typicode.com";
    public static String POSTS_ADDRESS = MAIN_DOMAIN_ADDRESS + "/posts";

    public static AsyncHttpClient getAsyncHttpClient() {
        return new AsyncHttpClient();
    }

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) MyApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
