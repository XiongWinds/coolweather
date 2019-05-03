package com.ypb.coolweather.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import com.ypb.coolweather.Constants.AreaLevel;
import com.ypb.coolweather.Constants.Constant;
import com.ypb.coolweather.Constants.DBManipulateType;
import com.ypb.coolweather.Constants.LogLevel;
import com.ypb.coolweather.db.DBhelper;
import com.ypb.coolweather.model.Area;
import com.ypb.coolweather.model.City;
import com.ypb.coolweather.model.County;
import com.ypb.coolweather.model.Province;
import com.ypb.coolweather.tools.HttpClient;
import com.ypb.coolweather.tools.ParseArea;
import com.ypb.coolweather.tools.impl.GenAreaImpl;
import com.ypb.coolweather.tools.inter.GenArea;
import com.ypb.coolweather.tools.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IndbMain implements  Runnable{

    private String  preProvince = "http://www.weather.com.cn/data/list3/city.xml";
    private String preCity = "http://www.weather.com.cn/data/list3/city";
    private String postCity = ".xml";
    private String preCounty = "http://www.weather.com.cn/data/list3/city";
    private String postCounty = ".xml";
    private String preWeather = "";
    private String postWeather = "";
    private DBhelper dbhelper = null;
    private boolean dbCreated = false;
    private Handler handler = null;

    private Map<Province, Set<Map<City,Set<County>>> > areas = new HashMap<>();
    Context cnt = null;

    Log log = Log.getInstance();

    public IndbMain(){}

    public IndbMain(Context cnt){
        this.cnt = cnt;
    }

    public IndbMain(Context cnt, Handler handler){
        this.cnt = cnt;
        this.handler = handler;
    }

    private String[] formDbTables(){
        String []dbTables  = new String[3];
        dbTables[0] = "create table province(tag integer primary key autoincrement,"
                      +"name text,superTag text)";
        dbTables[1] = "create table city(tag integer primary key autoincrement,"
                +"name text,superTag text)";;
        dbTables[2] = "create table county(tag integer primary key autoincrement,"
                +"name text,superTag text)";;
        return dbTables;
    }

    public boolean createDb(){
        String dbName = "wthr.db";
        int dbVersion = 1;
        DBhelper dBhelper = DBhelper.getInstance(cnt,dbName,null,dbVersion);
        dBhelper.setDBTables(formDbTables());
        dBhelper.getWritableDatabase();
        this.dbhelper = dBhelper;
        dbCreated = false;
        return true;
    }

    public int getData(){
        try {
            HttpClient httpClient = new HttpClient(preProvince);
            String provinceStr = httpClient.request();
            ParseArea parseArea = new ParseArea();
            parseArea.resetParameter(provinceStr,"");
            GenArea<Area> gen = new GenAreaImpl() ;
            ((GenAreaImpl) gen).setLevel(AreaLevel.PROVINCE);
            List<Province> listProvince = parseArea.parse(gen);
            for( Province province : listProvince){
                String tag = province.getTag();
                String cityUrl = this.preCity + tag + this.postCity;
                HttpClient httpClientCity = new HttpClient(cityUrl);
                String cityStr = httpClientCity.request();

                parseArea.resetParameter(cityStr,tag);
                ((GenAreaImpl) gen).setLevel(AreaLevel.CITY);
                List<City> listCity = parseArea.parse(gen);

                Set<Map<City,Set<County>>> setCityOwnCounties = new HashSet<Map<City,Set<County>>>();
                for( City city : listCity ) {
                    String tag4County = city.getTag();
                    String countyUrl = this.preCounty + tag4County + this.postCounty;
                    HttpClient httpClientCounty = new HttpClient(countyUrl);
                    String countyStr = httpClientCounty.request();
                    parseArea.resetParameter(countyStr,tag4County);
                    ((GenAreaImpl) gen).setLevel(AreaLevel.COUNTY);
                    List<County> listCounty = parseArea.parse(gen);
                    Set<County> setCounty = new HashSet<>(listCounty);

                    Map<City,Set<County>>  mapCityOwnCounties = new HashMap<City, Set<County>>();
                    mapCityOwnCounties.put(city,setCounty);
                    setCityOwnCounties.add(mapCityOwnCounties);
                }
                areas.put(province,setCityOwnCounties);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return areas.size();
    }

    public int insertDb(){
        Set<Province> setProvince = areas.keySet();

        List<ContentValues> listContentValues4Provinces = new ArrayList<ContentValues>();
        List<ContentValues> listContentValues4Cities = new ArrayList<ContentValues>();
        List<ContentValues> listContentValues4Counties = new ArrayList<ContentValues>();

        for( Province prvn : setProvince ){
            Set<Map<City,Set<County>>> setCityContainsCounties = areas.get(prvn);

            for( Map<City,Set<County>> mapCity : setCityContainsCounties ){

                Set<City> cities = mapCity.keySet();
                for( City city : cities ){
                    Set<County> setCounties = mapCity.get(city);
                    for( County county:setCounties){
                        ContentValues cntCounty = new ContentValues();
                        cntCounty.put("tag",county.getTag());
                        cntCounty.put("name",county.getName());
                        cntCounty.put("superTag",city.getTag());
                        listContentValues4Counties.add(cntCounty);
                    }
                    ContentValues cntCity = new ContentValues();
                    cntCity.put("tag",city.getTag());
                    cntCity.put("name",city.getName());
                    cntCity.put("superTag",prvn.getTag());
                    listContentValues4Cities.add(cntCity);
                }
            }
            ContentValues cntPrvn = new ContentValues();
            cntPrvn.put("tag",prvn.getTag());
            cntPrvn.put("name",prvn.getName());
            cntPrvn.put("superTag","");
            listContentValues4Provinces.add(cntPrvn);
        }
        if( this.dbhelper == null){
            log.print(LogLevel.DEBUG,"dbhelper==null");
            String dbName = "wthr.db";
            int dbVersion = 1;
            DBhelper dBhelper = DBhelper.getInstance(cnt,dbName,null,dbVersion);
            dBhelper.setDBTables(formDbTables());
            dBhelper.getWritableDatabase();
            this.dbhelper = dBhelper;
        }
        dbhelper.insertSql("province",listContentValues4Provinces);
        dbhelper.insertSql("city",listContentValues4Cities);
        dbhelper.insertSql("county",listContentValues4Counties);

        return listContentValues4Counties.size();

    }
    @Override
    public void run() {
        if (!dbCreated) {
            Log log = Log.getInstance();
            log.print(LogLevel.DEBUG, "before createDb");
            //建库
            createDb();
            log.print(LogLevel.DEBUG, "after createDb");
            log.print(LogLevel.DEBUG, "before getData");
            //获取数据
            getData();
            log.print(LogLevel.DEBUG, "after getData");
            log.print(LogLevel.DEBUG, "before insertDb");
            //入库
            insertDb();
            log.print(LogLevel.DEBUG, "after insertDb");
            dbCreated = true;

            Message msg = new Message();
            msg.what = Constant.CREATE_DB;
            msg.obj = new Object();
            handler.sendMessage(msg);
        }
    }


}
