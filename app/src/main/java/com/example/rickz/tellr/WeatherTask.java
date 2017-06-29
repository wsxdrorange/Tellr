package com.example.rickz.tellr;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

/**
 * Created by rickz on 6/27/2017.
 */

public class WeatherTask extends AsyncTask<String, Void, JSONArray> {
    private JSONArray query;
    private Context context;
   // TextView tv = (TextView) findViewById(R.id.randomTextView);
    public WeatherTask(Context context){
        this.context = context;
    }
    protected JSONArray doInBackground(String... urls){
            JSONArray array = null;
            String link = urls[0];
            try{
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(con.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(in);
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                }
                JSONObject parentObj = new JSONObject(buffer.toString());
                JSONObject obj = parentObj.getJSONObject("daily");
                array = obj.getJSONArray("data");
                System.out.println(array);
                System.out.println("array");
                Log.d("myTag","Message");
            }
            catch (Exception e){

            }
            return array;
    }
    protected void onPostExecute(JSONArray result){
        query = result;
    }
}
