package com.ypb.coolweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ypb.coolweather.Constants.AreaLevel;
import com.ypb.coolweather.Constants.Constant;
import com.ypb.coolweather.Constants.LogLevel;
import com.ypb.coolweather.db.DBhelper;
import com.ypb.coolweather.model.Area;
import com.ypb.coolweather.services.GetWeatherInfo;
import com.ypb.coolweather.tools.IndbMain;
import com.ypb.coolweather.tools.util.Log;

import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {

    private TextView textView = null;
    private ListView listView = null;
    private SQLiteDatabase db = null;
    private Log log = null;
    private List<Area> listArea = null;
    private DBhelper dBhelper = null;
    private ArrayAdapter<Area> adapter = null;
    private Context outerContent = this;
    private Handler handler = null;

    private final String dbName = "wthr.db";
    private final int dbVersion = 1;
    public static final String EXTRA_MESSAGE = "COM_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        log = Log.getInstance();

        textView = findViewById(R.id.title_content);
        listView = findViewById(R.id.cities_list);

        textView.setText("中国");
        dBhelper = DBhelper.getInstance(this,dbName,null,dbVersion);
        listArea = dBhelper.queryCitiesByLevel(AreaLevel.PROVINCE,"");

        adapter = new ArrayAdapter<Area>(this,android.R.layout.simple_list_item_1,listArea);
        listView.setAdapter(adapter);

        handler = new Handler(){
            public void handleMessage(Message msg){
                if(msg.what == Constant.WEATHER_DETAIL_INFO)
                {
                    String weatherInfo = (String) msg.obj;
                    Intent intent = new Intent(outerContent, WeatherActivity.class);
                    Log.getInstance().print(LogLevel.DEBUG,"parameter........"+weatherInfo);

                    intent.putExtra(EXTRA_MESSAGE, weatherInfo);
                    startActivity(intent);
                }else if( msg.what == Constant.CREATE_DB ){
                    dBhelper = DBhelper.getInstance(outerContent,dbName,null,dbVersion);
                    Log.getInstance().print(LogLevel.DEBUG,Integer.valueOf(listArea.size()).toString());
                    listArea = dBhelper.queryCitiesByLevel(AreaLevel.PROVINCE,"");
                    //adapter = new ArrayAdapter<Area>(outerContent,android.R.layout.simple_list_item_1,listArea);
                    //listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    listView.setSelection(0);
                }
            }
        };

        Thread thread = new Thread(new IndbMain(this,handler));
        thread.start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Area area = (Area)listView.getAdapter().getItem(position);

                if( area instanceof com.ypb.coolweather.model.County ){
                    Log.getInstance().print(LogLevel.DEBUG,"county........");
                    GetWeatherInfo getWeatherInfo = new GetWeatherInfo(area.getTag(),handler);
                    new Thread(getWeatherInfo).start();
                }else if ( area instanceof com.ypb.coolweather.model.City ){
                    String superTag = area.getTag();
                    String name = area.getName();
                    textView.setText(name);
                    listArea = dBhelper.queryCitiesByLevel(AreaLevel.COUNTY,String.format("%04d",Integer.valueOf(superTag)));
                    //adapter = new ArrayAdapter<Area>(outerContent,android.R.layout.simple_list_item_1,listArea);
                    //listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else if( area instanceof com.ypb.coolweather.model.Province ){
                    String superTag = area.getTag();
                    String name = area.getName();
                    textView.setText(name);
                    listArea = dBhelper.queryCitiesByLevel(AreaLevel.CITY,String.format("%02d",Integer.valueOf(superTag)));
                    //adapter = new ArrayAdapter<Area>(outerContent,android.R.layout.simple_list_item_1,listArea);
                    //listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

}
