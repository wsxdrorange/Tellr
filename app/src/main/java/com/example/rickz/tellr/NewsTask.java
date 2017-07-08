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

public class NewsTask extends AsyncTask<String, Void, JSONArray[]> {
    private JSONArray  [] queries;

    // TextView tv = (TextView) findViewById(R.id.randomTextView);
    protected JSONArray [] doInBackground(String... urls){
        JSONArray [] array = new JSONArray[urls.length];
        String link;
        try{
            for (int i = 0; i < urls.length; i++){
                link = urls[i];
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
                JSONArray arr = parentObj.getJSONArray("articles");
                array[i] = arr;
            }
            //System.out.println("CHECK1");
        }
        catch (Exception e){

        }
        return array;
    }
    protected void onPostExecute(JSONArray  [] result){
        this.queries = result;
        //System.out.println(query);
    }
    public JSONArray [] getQuery() {
        return this.queries;
    }
}
