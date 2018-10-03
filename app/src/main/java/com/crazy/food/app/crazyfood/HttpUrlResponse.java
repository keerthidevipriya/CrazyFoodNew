package com.crazy.food.app.crazyfood;

import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpUrlResponse {
    final static String FILTER="json/v1/1/filter.php";
    final static String LOOK_UP="json/v1/1/lookup.php";

    public static URL buildURL(String impurl, String mainPurpose) {
        Uri buildUri = Uri.parse(impurl)
                .buildUpon()
                .appendPath("api").appendPath(FILTER)
                .appendQueryParameter("c",mainPurpose)
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    //https://www.themealdb.com/api/json/v1/1/lookup.php?i=52956
    public static URL buildLookupUrl(String impurl,String sid){
        Uri buildUri = Uri.parse(impurl)
                .buildUpon()
                .appendPath("api").appendPath(LOOK_UP)
                .appendQueryParameter("i",sid)
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        String responseHttp = null;
        try {
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            responseHttp=streamStringConversion(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseHttp;
    }

    private static String streamStringConversion(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
