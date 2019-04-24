package com.ypb.coolweather.tools;

import com.ypb.coolweather.Constants.LogLevel;
import com.ypb.coolweather.model.Area;
import com.ypb.coolweather.tools.inter.GenArea;
import com.ypb.coolweather.tools.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ParseArea<T> {

    private String origString = "";
    private String superTag = "";
    private Log log = Log.getInstance();

    public ParseArea(){ }

    public ParseArea(String orStr){ this.origString = orStr; }

    public void resetParameter(String orStr,String superTag){
        this.origString = orStr;
        this.superTag = superTag;
    }

    public  List<T> parse(GenArea<T> genArea){
        List<T> listArea = new ArrayList<T>();

        //log.print(LogLevel.DEBUG,origString);
        String [] areas = Pattern.compile(",").split(origString);
        for(int i = 0 ; i < areas.length ; ++i ) {
            log.print(LogLevel.DEBUG,areas[i]);
            String tag = (Pattern.compile("\\|").split(areas[i]))[0];
            String name = (Pattern.compile("\\|").split(areas[i]))[1];

            T t = genArea.genNext(name,tag,superTag);
            listArea.add(t);
        }
        return listArea;
    }

}
