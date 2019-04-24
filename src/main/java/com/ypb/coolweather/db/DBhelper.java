package com.ypb.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.ypb.coolweather.Constants.DBManipulateType;
import com.ypb.coolweather.model.Area;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DBhelper extends SQLiteOpenHelper {

    private String dbName = "";
    private String dbVersion = "";
    private Context context = null;
    private String []dbTables = new String[3];
    private SQLiteDatabase db = null;

    public DBhelper(Context cnt,String name,CursorFactory factory, int version){
        super(cnt,name,factory,version);
        this.dbName = dbName;
        this.dbVersion = dbVersion;
        context = cnt;
    }

    public void setDBTables(String []dbTables){
        this.dbTables = dbTables;
    }

    public String[] getDBTables(){
        return this.dbTables;
    }

    public boolean execSql(DBManipulateType manipulateType, String table, List<ContentValues> listContentValues ){
        if(db == null) {
            db = this.getWritableDatabase();
        }
        switch( manipulateType ){
            case INSERT:
                for( ContentValues cv : listContentValues ){
                    db.insert(table,"",cv);
                }
                break;
            case SELECT:
                break;
            default:
                break;
        }
        return true;
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
