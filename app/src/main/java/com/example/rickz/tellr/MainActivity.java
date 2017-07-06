package com.example.rickz.tellr;

import android.content.Context;
import android.content.IntentSender;
import android.content.SyncStatusObserver;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    WeatherTask weather;
    String news;
    String name = "Rick Zhang";
    double currentLatitude;
    double currentLongitude;

    //TTS Service
    TextToSpeech engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        news = "Hello " + name + ". Here is your daily update. ";
        weather = new WeatherTask();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        currentLongitude = location.getLongitude();
        currentLatitude = location.getLatitude();
        weather.execute("https://api.darksky.net/forecast/f87e64592db4c029937c3c7e92f9e115/" + currentLatitude + "," + currentLongitude);
        Log.d("myTag", currentLatitude + " " + currentLatitude);

        //Init TTS
        engine = new TextToSpeech(this, this);





        //Tell me Button
        Button tellMe = (Button) findViewById(R.id.tellMe);
        tellMe.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View v) {
                addWeatherStatement();
                System.out.println(news);
                engine.speak(news,TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

    }

    public void addWeatherStatement() {
        int minTemp, maxTemp, precip;
        String summary;

        JSONArray array = weather.getQuery(); //get JSON array from WeatherTask
        //System.out.println("ARRAY: \n" + array);
        try {
            //Add weather statement from JSCON object
            minTemp = (int) Double.parseDouble(array.getJSONObject(0).getString("temperatureMin")); //get low Temp
            maxTemp = (int) Double.parseDouble(array.getJSONObject(0).getString("temperatureMax")); //get High Temp
            precip = (int) (Double.parseDouble(array.getJSONObject(0).getString("precipProbability")) * 10.0); //get chance of rain
            summary = array.getJSONObject(0).getString("summary").toString(); //get daily summary
            //System.out.println(minTemp + "\n" + maxTemp + "\n" + precip + "\n" + summary);

            //Update news message
            news += "Today's weather will be " + summary + " The high for today is " + maxTemp + " degrees Fahrenheit, and the low for today is " + minTemp + ". There is a " + precip + " percent chance that it will rain today.";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onInit(int status){
        if (status == TextToSpeech.SUCCESS){
            Log.d("TextToSpeech","ONINIT Success");
            engine.setLanguage(Locale.US);
        }
    }
}
