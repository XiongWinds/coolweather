package com.ypb.coolweather.model;

import com.ypb.coolweather.Constants.AreaLevel;

public abstract  class Area {


    public abstract  void setName(String name);

    public abstract void setTag(String tag);

    public abstract String getName();

    public abstract String getTag();

    public abstract void setSuperTag(String tag);

    public abstract String getSuperTag();

    public static Area genArea(AreaLevel areaLevel){
        switch (areaLevel){
            case PROVINCE:
                return new Province();
            case CITY:
                return new City();
            case COUNTY:
                return new County();
            default:
                return new Province();
        }
    }
}
