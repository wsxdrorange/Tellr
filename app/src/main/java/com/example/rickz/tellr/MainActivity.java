package com.example.rickz.tellr;

import android.content.SyncStatusObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;


public class MainActivity extends AppCompatActivity{

    WeatherTask weather;
    String news;
    String name = "Rick Zhang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        news = "Hello " + name + ". Here is your daily update. ";
        weather = new WeatherTask();
        weather.execute("https://api.darksky.net/forecast/f87e64592db4c029937c3c7e92f9e115/33.2155920,-97.1460380");
        Log.d("myTag","Finished On Create");



        //Tell me Button
        Button tellMe = (Button) findViewById(R.id.tellMe);
        tellMe.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                addWeatherStatement();
                System.out.println(news);

            }
        });

    }
    public void addWeatherStatement(){
        int minTemp,maxTemp,precip;
        String summary;

        JSONArray array = weather.getQuery(); //get JSON array from WeatherTask
        //System.out.println("ARRAY: \n" + array);
        try {
            //Add weather statement from JSCON object
            minTemp = (int)Double.parseDouble(array.getJSONObject(0).getString("temperatureMin")); //get low Temp
            maxTemp = (int)Double.parseDouble(array.getJSONObject(0).getString("temperatureMax")); //get High Temp
            precip = (int)(Double.parseDouble(array.getJSONObject(0).getString("precipProbability"))*10.0); //get chance of rain
            summary = array.getJSONObject(0).getString("summary").toString(); //get daily summary
            //System.out.println(minTemp + "\n" + maxTemp + "\n" + precip + "\n" + summary);

            //Update news message
            news += "Today's weather will be " + summary + " The high for today is " + maxTemp + " degrees Fahrenheit, and the low for today is " + minTemp + ". There is a " + precip + " percent chance that it will rain today.";
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
