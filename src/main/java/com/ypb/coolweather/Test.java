package com.ypb.coolweather;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Test {


    public static void main(String args[]){

        Map<String,String> mapWeather = new HashMap<String,String>();
        String s = "{\"weatherinfo\":{\"city\":\"北京\",\"cityid\":\"101010100\",\"temp1\":\"-2℃\",\"temp2\":\"16℃\",\"weather\":\"晴\",\"img1\":\"n0.gif\",\"img2\":\"d0.gif\",\"ptime\":\"18:00\"}}";
        System.out.println(s);
        String weather = (s.split(":\\{") )[1];
        String []ss =  weather.substring(0,weather.length()-2).split(",");
        for(String sss:ss){
            System.out.println(sss);
            String key = (sss.split(":"))[0];
            String value = (sss.split(":"))[1];
            mapWeather.put( Test.trim(key,"\""),Test.trim(value,"\"") );
        }
    }

    private static String trim(String orgin,String token){
        System.out.println("index=="+Integer.valueOf( orgin.indexOf(token) ).toString());

        while( orgin.indexOf(token) >= 0 ){
            orgin = orgin.substring(orgin.indexOf(token)+1,orgin.length() - 1);
            System.out.println(orgin);
        }
        return orgin;
    }
}
