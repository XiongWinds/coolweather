package com.ypb.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.ypb.coolweather.Constants.AreaLevel;
import com.ypb.coolweather.Constants.DBManipulateType;
import com.ypb.coolweather.model.Area;
import com.ypb.coolweather.model.Province;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DBhelper extends SQLiteOpenHelper {

    private String dbName = "";
    private String dbVersion = "";
    private Context context = null;
    private String []dbTables = new String[3];
    private SQLiteDatabase db = null;
    private static DBhelper dbhelper = null;
    private static String[] formDbTables(){
        String []dbTables  = new String[3];
        dbTables[0] = "create table province(tag integer primary key autoincrement,"
                +"name text,superTag text)";
        dbTables[1] = "create table city(tag integer primary key autoincrement,"
                +"name text,superTag text)";;
        dbTables[2] = "create table county(tag integer primary key autoincrement,"
                +"name text,superTag text)";;
        return dbTables;
    }


    private DBhelper(Context cnt,String name,CursorFactory factory, int version){
        super(cnt,name,factory,version);
        this.dbName = dbName;
        this.dbVersion = dbVersion;
        context = cnt;
    }

    public static DBhelper getInstance(Context cnt,String name,CursorFactory factory, int version){
        if(dbhelper == null) {
            dbhelper = new DBhelper(cnt, name, factory, version);
            dbhelper.setDBTables(formDbTables());
        }
        return dbhelper;
    }

    public void setDBTables(String []dbTables){
        this.dbTables = dbTables;
    }

    public String[] getDBTables(){
        return this.dbTables;
    }

    public int insertSql(String table, List<ContentValues> listContentValues ){
        if(db == null) {
            db = this.getWritableDatabase();
        }
        for( ContentValues cv : listContentValues ){
            db.insert(table,"",cv);
        }
        return listContentValues.size();
    }

    public Cursor querySql(String table, String []columns, String cond, String []condArgs){
        if(db == null) {
            db = this.getWritableDatabase();
        }
        return db.query(table,columns,cond,condArgs,"","","","");
    }

    public List<Area> queryCitiesByLevel(AreaLevel areaLevel,String superTag){

        String table="";
        String []columns = new String[]{"tag","name","superTag"};
        String cond = " superTag = ? ";
        String []condArgs = new String[]{superTag};

        List<Area> listCities = new ArrayList<Area>();
        switch (areaLevel){
            case PROVINCE:
                table = "province";
                break;
            case CITY:
                table = "city";
                break;
            case COUNTY:
                table = "county";
                break;
            default:
                break;
        }

        Cursor cursor = querySql(table,columns,cond,condArgs);
        if( cursor.moveToFirst() ){

            do{
                Area area = Area.genArea(areaLevel);
                area.setSuperTag(cursor.getString(cursor.getColumnIndex("superTag")));
                area.setTag(cursor.getString(cursor.getColumnIndex("tag")));
                area.setName(cursor.getString(cursor.getColumnIndex("name")));
                listCities.add(area);
            }while( cursor.moveToNext() );
        }
        return listCities;
    }

    public List<ContentValues> convertAreasToContentValues(List<Area> list){
        List<ContentValues> listContentValues = new ArrayList<ContentValues>();

        for( int i = 0;i < list.size() ; ++i )
        {
            Area area = list.get(i);
            String name = area.getName();
            String tag = area.getTag();
            String superTag = area.getSuperTag();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name",name);
            contentValues.put("tag",tag);
            contentValues.put("superTag",superTag);
            listContentValues.add(contentValues);
        }
        return listContentValues;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        for( int i = 0 ; i < this.dbTables.length ; ++i )
        {
            db.execSQL( dbTables[i] );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
