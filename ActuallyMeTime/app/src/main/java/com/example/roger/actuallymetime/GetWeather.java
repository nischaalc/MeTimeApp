package com.example.roger.actuallymetime;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by gofar on 8/27/2015.
 */
class GetWeather extends AsyncTask<Void, Void, ArrayList<String[]>> {
    String key = "9809bc487730bcb2";
    JsonArray forecastDayArr;
    JsonObject forecastDay;
    String high;
    String low;
    String conditions;
    String iconUrl;
    String site;
    String zip;
    HttpURLConnection request;
    URL url;
    JsonParser jp;
    InputStreamReader isr;
    ArrayList<String[]> ret = new ArrayList<>();
    ArrayList<Bitmap> imgList = new ArrayList<>();
    Context c;

    public GetWeather(MainActivity mainActivity) {
        c = mainActivity;
    }

    @Override
    protected ArrayList<String[]> doInBackground(Void... voids) {
        try {
            site = "http://api.wunderground.com/api/" + key + "/geolookup/q/autoip.json";

            //connect to the URL
            url = new URL(site);
            request = (HttpURLConnection) url.openConnection();
            request.connect();

            //get the time from an origin to a destination using Google's driving directions
            isr = new InputStreamReader((InputStream) request.getContent());
            jp = new JsonParser();

            zip = jp.parse(isr).getAsJsonObject().get("location").getAsJsonObject().get("zip").getAsString();

            site = "http://api.wunderground.com/api/" + key + "/forecast10day/q/" + zip + ".json";

            //connect to the URL
            url = new URL(site);
            request = (HttpURLConnection) url.openConnection();
            request.connect();

            //get the time from an origin to a destination using Google's driving directions
            isr = new InputStreamReader((InputStream) request.getContent());
            jp = new JsonParser();

            forecastDayArr = jp.parse(isr).getAsJsonObject().get("forecast").getAsJsonObject()
                    .get("simpleforecast").getAsJsonObject().get("forecastday").getAsJsonArray();

            for(int i=0; i<7; i++){
                String[] dayWeatherArr = new String[3];

                forecastDay = forecastDayArr.get(i).getAsJsonObject();

                high = forecastDay.get("high").getAsJsonObject().get("fahrenheit").getAsString();
                low = forecastDay.get("low").getAsJsonObject().get("fahrenheit").getAsString();
                conditions = forecastDay.get("conditions").getAsString();
                iconUrl = forecastDay.get("icon_url").getAsString();

                dayWeatherArr[0] = high;
                dayWeatherArr[1] = low;
                dayWeatherArr[2] = conditions;

                ret.add(dayWeatherArr);

                try {
                    //get the image from the website returned in the JSON
                    URL iu = new URL(iconUrl);//icon
                    URLConnection connection = null;
                    connection = iu.openConnection();

                    connection.connect();

                    InputStream inputStream = connection.getInputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    //set the image to the bitmap arraylist
                    imgList.add(BitmapFactory.decodeStream(bufferedInputStream));

                    bufferedInputStream.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return ret;
        } catch (Exception ex) {
            Log.i("ERROR", ex.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<String[]> param) {
        super.onPostExecute(param);

        ImageView day1Pic = (ImageView)((Activity) c).getWindow().findViewById(R.id.day1pic);
        day1Pic.setImageBitmap(imgList.get(0));
        ImageView day2Pic = (ImageView)((Activity) c).getWindow().findViewById(R.id.day2pic);
        day2Pic.setImageBitmap(imgList.get(1));
        ImageView day3Pic = (ImageView)((Activity) c).getWindow().findViewById(R.id.day3pic);
        day3Pic.setImageBitmap(imgList.get(2));
        ImageView day4Pic = (ImageView)((Activity) c).getWindow().findViewById(R.id.day4pic);
        day4Pic.setImageBitmap(imgList.get(3));
        ImageView day5Pic = (ImageView)((Activity) c).getWindow().findViewById(R.id.day5pic);
        day5Pic.setImageBitmap(imgList.get(4));
        ImageView day6Pic = (ImageView)((Activity) c).getWindow().findViewById(R.id.day6pic);
        day6Pic.setImageBitmap(imgList.get(5));
        ImageView day7Pic = (ImageView)((Activity) c).getWindow().findViewById(R.id.day7pic);
        day7Pic.setImageBitmap(imgList.get(6));

        /*
        TextView day1 = (TextView)((Activity) c).getWindow().findViewById(R.id.day1NextClass);
        day1.setText(param.get(0)[0]);
        TextView day2 = (TextView)((Activity) c).getWindow().findViewById(R.id.day2NextClass);
        day2.setText(param.get(1)[0]);
        TextView day3 = (TextView)((Activity) c).getWindow().findViewById(R.id.day3NextClass);
        day3.setText(param.get(2)[0]);
        TextView day4 = (TextView)((Activity) c).getWindow().findViewById(R.id.day4NextClass);
        day4.setText(param.get(3)[0]);
        TextView day5 = (TextView)((Activity) c).getWindow().findViewById(R.id.day5NextClass);
        day5.setText(param.get(4)[0]);
        TextView day6 = (TextView)((Activity) c).getWindow().findViewById(R.id.day6NextClass);
        day6.setText(param.get(5)[0]);
        TextView day7 = (TextView)((Activity) c).getWindow().findViewById(R.id.day7NextClass);
        day7.setText(param.get(6)[0]);
        */

        TextView day1 = (TextView)((Activity) c).getWindow().findViewById(R.id.day1High);
        day1.setText("High: " + param.get(1)[0]);
        TextView day2 = (TextView)((Activity) c).getWindow().findViewById(R.id.day2High);
        day2.setText("High: " + param.get(1)[0]);
        TextView day3 = (TextView)((Activity) c).getWindow().findViewById(R.id.day3High);
        day3.setText("High: " + param.get(2)[0]);
        TextView day4 = (TextView)((Activity) c).getWindow().findViewById(R.id.day4High);
        day4.setText("High: " + param.get(3)[0]);
        TextView day5 = (TextView)((Activity) c).getWindow().findViewById(R.id.day5High);
        day5.setText("High: " + param.get(4)[0]);
        TextView day6 = (TextView)((Activity) c).getWindow().findViewById(R.id.day6High);
        day6.setText("High: " + param.get(5)[0]);
        TextView day7 = (TextView)((Activity) c).getWindow().findViewById(R.id.day7High);
        day7.setText("High: " + param.get(6)[0]);

        day1 = (TextView)((Activity) c).getWindow().findViewById(R.id.day1Low);
        day1.setText("Low: " + param.get(0)[1]);
        day2 = (TextView)((Activity) c).getWindow().findViewById(R.id.day2Low);
        day2.setText("Low: " + param.get(1)[1]);
        day3 = (TextView)((Activity) c).getWindow().findViewById(R.id.day3Low);
        day3.setText("Low: " + param.get(2)[1]);
        day4 = (TextView)((Activity) c).getWindow().findViewById(R.id.day4Low);
        day4.setText("Low: " + param.get(3)[1]);
        day5 = (TextView)((Activity) c).getWindow().findViewById(R.id.day5Low);
        day5.setText("Low: " + param.get(4)[1]);
        day6 = (TextView)((Activity) c).getWindow().findViewById(R.id.day6Low);
        day6.setText("Low: " + param.get(5)[1]);
        day7 = (TextView)((Activity) c).getWindow().findViewById(R.id.day7Low);
        day7.setText("Low: " + param.get(6)[1]);

        day1 = (TextView)((Activity) c).getWindow().findViewById(R.id.day1Condition);
        day1.setText(param.get(0)[2]);
        day2 = (TextView)((Activity) c).getWindow().findViewById(R.id.day2Condition);
        day2.setText(param.get(1)[2]);
        day3 = (TextView)((Activity) c).getWindow().findViewById(R.id.day3Condition);
        day3.setText(param.get(2)[2]);
        day4 = (TextView)((Activity) c).getWindow().findViewById(R.id.day4Condition);
        day4.setText(param.get(3)[2]);
        day5 = (TextView)((Activity) c).getWindow().findViewById(R.id.day5Condition);
        day5.setText(param.get(4)[2]);
        day6 = (TextView)((Activity) c).getWindow().findViewById(R.id.day6Condition);
        day6.setText(param.get(5)[2]);
        day7 = (TextView)((Activity) c).getWindow().findViewById(R.id.day7Condition);
        day7.setText(param.get(6)[2]);
    }
}