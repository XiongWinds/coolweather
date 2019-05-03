package com.ypb.coolweather.tools;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class HttpClient {

    private HttpURLConnection urlConnection = null;
    private BufferedReader dataInputStream = null;
    private DataOutputStream dataOutputStream = null;
    private String content;

    public HttpClient(){
    }

    public HttpClient(String url) throws  IOException{
        if( this.urlConnection == null ){
            URL lurl = new URL(url);
            this.urlConnection = (HttpURLConnection)lurl.openConnection();
            this.urlConnection.setRequestMethod("GET");
            this.urlConnection.setReadTimeout(8000);
            this.urlConnection.setConnectTimeout(8000);

            dataInputStream = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            //dataOutputStream = new DataOutputStream(this.urlConnection.getOutputStream());
        }
    }

    public String request() throws IOException {

        StringBuffer sb = new StringBuffer(10000);
        String line ;
        while( ( line = this.dataInputStream.readLine() ) != null ){
            sb.append(line);
        }
        this.urlConnection.disconnect();
        return sb.toString();

    }

    public static void main(String []args){
        String urls[] = new String[5];
        urls[0] = "http://www.weather.com.cn/data/list3/city.xml";
        urls[1] = "http://www.weather.com.cn/data/list3/city16.xml";
        urls[2] = "http://www.weather.com.cn/data/list3/city1601.xml";
        urls[3] = "http://www.weather.com.cn/data/list3/city160101.xml";
        urls[4] = "http://www.weather.com.cn/data/cityinfo/101160101.html";

        for( int i = 0 ; i < urls.length ; ++i ){
            HttpClient httpClient = null;
            try {
                httpClient = new HttpClient(urls[i]);
                System.out.println( httpClient.request() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
