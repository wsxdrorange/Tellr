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

import static android.R.attr.name;

public class MainActivity extends AppCompatActivity{

    WeatherTask weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String news = "Hello " + name + ". Here is your daily update.";
        weather = new WeatherTask();
        weather.execute("https://api.darksky.net/forecast/f87e64592db4c029937c3c7e92f9e115/33.2155920,-97.1460380");
        Log.d("myTag","message");



        //Tell me Button
        Button tellMe = (Button) findViewById(R.id.tellMe);
        tellMe.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                run();

            }
        });

    }
    public void run(){
        JSONArray array = weather.getQuery();
        //System.out.println("ARRAY: \n" + array);
        try {
            System.out.println(array.getJSONObject(0).getString("temperatureMin"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
