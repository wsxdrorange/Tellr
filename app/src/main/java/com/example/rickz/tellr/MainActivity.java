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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    WeatherTask weather;
    NewsTask newsTask;
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

        Log.d("DEBUG","START");
        news = "Hello " + name + ". Here is your daily update. ";

        //Weather
        weather = new WeatherTask();
        Log.d("DEBUG", currentLatitude + " " + currentLatitude);
        weather.execute("https://api.darksky.net/forecast/f87e64592db4c029937c3c7e92f9e115/" + currentLatitude + "," + currentLongitude);

        //News
        String [] newsAPI = {"https://newsapi.org/v1/articles?source=cnn&sortBy=top&apiKey=2bc8915e285b49778b1b8314aedcb621","https://newsapi.org/v1/articles?source=espn&sortBy=top&apiKey=2bc8915e285b49778b1b8314aedcb621"," https://newsapi.org/v1/articles?source=mtv-news&sortBy=top&apiKey=2bc8915e285b49778b1b8314aedcb621","https://newsapi.org/v1/articles?source=techcrunch&sortBy=top&apiKey=2bc8915e285b49778b1b8314aedcb621"};
        newsTask = new NewsTask();
        newsTask.execute(newsAPI);


        //Init TTS
        engine = new TextToSpeech(this, this);





        //Tell me Button
        Button tellMe = (Button) findViewById(R.id.tellMe);
        tellMe.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View v) {
                addWeatherStatement();
                news += "Here is your news for today. ";
                addNewsStatement();

                //End news
                news += "Stay productive and have a great day!";
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
    public void addNewsStatement() {
        String headline, description;

        JSONArray [] array = newsTask.getQuery();
            try {
                //General News
                JSONArray currArr = array[0];
                headline = currArr.getJSONObject(0).getString("title");
                description = currArr.getJSONObject(0).getString("description");
                news += headline + ". " + description + " ";

                //sports
                currArr = array[1];
                headline = currArr.getJSONObject(0).getString("title");
                description = currArr.getJSONObject(0).getString("description");
                news += "Sports. " + headline + ". " + description + " ";

                //entertainment
                currArr = array[2];
                headline = currArr.getJSONObject(0).getString("title");
                description = currArr.getJSONObject(0).getString("description");
                news += "Entertainment. " + headline + ". " + description + " ";

                //tech
                currArr = array[3];
                headline = currArr.getJSONObject(0).getString("title");
                description = currArr.getJSONObject(0).getString("description");
                news += "Tech. " + headline + ". " + description + " ";
            }
            catch(JSONException e) {
                e.printStackTrace();
                ;
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
