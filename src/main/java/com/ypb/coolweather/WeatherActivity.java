package com.ypb.coolweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ypb.coolweather.Constants.AreaLevel;
import com.ypb.coolweather.Constants.LogLevel;
import com.ypb.coolweather.db.DBhelper;
import com.ypb.coolweather.model.Area;
import com.ypb.coolweather.tools.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;


public class WeatherActivity extends Activity {

    private TextView weatherDesc = null;
    private TextView tempLow = null;
    private TextView tempHigh = null;
    private TextView cityId = null;
    private Map<String,String> mapWeather = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_report);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        Log.getInstance().print(LogLevel.DEBUG,"message======"+message);

        Map<String,String> weather = parseWeatherInfo(message);

        tempLow = findViewById(R.id.templow);
        tempHigh = findViewById(R.id.temphigh);
        cityId = findViewById(R.id.city_id);
        weatherDesc = findViewById(R.id.weatherdesc);

        tempLow.setText(weather.get("temp1"));
        tempHigh.setText(weather.get("temp2"));
        cityId.setText(weather.get("city"));
        weatherDesc.setText(weather.get("weather"));

    }

    private Map<String,String> parseWeatherInfo(String weather){
        String weatherinfo = (weather.split(":\\{") )[1];
        String []ss =  weatherinfo.substring(0,weatherinfo.length()-2).split(",");
        for(String sss:ss){
            String key = (sss.split(":"))[0];
            String value = (sss.split(":"))[1];
            mapWeather.put( trim(key,"\""),trim(value,"\"") );
        }
        return mapWeather;
    }

    private String trim(String orgin,String token){
        while( orgin.indexOf(token) >= 0 ){
            orgin = orgin.substring(orgin.indexOf(token)+1,orgin.length() - 1);
            Log.getInstance().print(LogLevel.DEBUG,orgin);
        }
        return orgin;
    }

}
