package com.ypb.coolweather.tools.util;

import com.ypb.coolweather.Constants.LogLevel;

public class Log {

    private final LogLevel level = LogLevel.DEBUG ;
    private static Log log = null;

    private Log(){ }



    public  static Log getInstance(){
        if( log == null )
            log = new Log();
        return log;
    }

    public void print(LogLevel lgLevel,String ss){
        if( lgLevel.compareTo(level) >= 0 ){
            System.out.println(ss);
        }
    }
}
