package com.ypb.coolweather.services;

import android.os.Handler;
import android.os.Message;

import com.ypb.coolweather.Constants.Constant;
import com.ypb.coolweather.Constants.LogLevel;
import com.ypb.coolweather.tools.HttpClient;
import com.ypb.coolweather.tools.util.Log;

import java.io.IOException;
import java.util.regex.Pattern;

public class GetWeatherInfo implements Runnable{

    private String cityTag = "";
    private String weatherTag = "";
    private Handler handler = null;
    private final String preWeatherDetailUrl = "http://www.weather.com.cn/data/cityinfo/";
    private final String postWeatherDetailUrl = ".html";

    private final String preWeatherUrl = "http://www.weather.com.cn/data/list3/city";
    private final String postWeatherUrl = ".xml";

    HttpClient httpClient = null;

    public GetWeatherInfo(){}

    public GetWeatherInfo(String cityTag){this.cityTag = cityTag;}

    public GetWeatherInfo(String cityTag, Handler handler){
        this.cityTag = cityTag;
        this.handler = handler;
    }

    private String getWeatherTag(){
        String weatherTag = "";
        try {
            httpClient = new HttpClient(preWeatherUrl+cityTag+postWeatherUrl);
            Log.getInstance().print(LogLevel.DEBUG, "url=="+preWeatherUrl+cityTag+postWeatherUrl );
            weatherTag = httpClient.request();
            Log.getInstance().print(LogLevel.DEBUG, weatherTag );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherTag;
    }

    private String getWeatherInfo(){
        String weatherInfo = "";
        try {
            Log.getInstance().print(LogLevel.DEBUG,"weatherTag=="+(Pattern.compile("\\|").split(weatherTag))[1]);
            httpClient = new HttpClient(preWeatherDetailUrl+(Pattern.compile("\\|").split(weatherTag))[1]+postWeatherDetailUrl);
            Log.getInstance().print(LogLevel.DEBUG,preWeatherDetailUrl+(Pattern.compile("\\|").split(weatherTag))[1]+postWeatherDetailUrl);
            weatherInfo = httpClient.request();
            Log.getInstance().print(LogLevel.DEBUG,weatherInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.getInstance().print(LogLevel.DEBUG, (Pattern.compile("\\|").split(weatherInfo))[0] );
        return weatherInfo;
    }

    @Override
    public void run() {
        weatherTag = getWeatherTag();
        String weatherInfo = getWeatherInfo();
        Message msg = new Message();
        msg.what = Constant.WEATHER_DETAIL_INFO;
        msg.obj = (Object)weatherInfo ;
        handler.sendMessage(msg);
    }

    public static void main(String args[]){
        GetWeatherInfo getWeatherInfo = new GetWeatherInfo("160101");
        new Thread(getWeatherInfo).start();

    }
}
