package com.example.rickz.tellr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.net.URL;

import static android.R.attr.name;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Tell me Button
        Button tellMe = (Button) findViewById(R.id.tellMe);
        tellMe.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String news = "Hello " + name + ". Here is your daily update.";
                //Generate String
                new WeatherTask(MainActivity.this).execute("https://api.darksky.net/forecast/f87e64592db4c029937c3c7e92f9e115/37.8267,-122.4233");


            }
        });

    }

}
