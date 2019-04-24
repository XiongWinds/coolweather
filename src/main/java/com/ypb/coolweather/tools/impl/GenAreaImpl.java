package com.ypb.coolweather.tools.impl;

import com.ypb.coolweather.Constants.AreaLevel;
import com.ypb.coolweather.model.Area;
import com.ypb.coolweather.model.City;
import com.ypb.coolweather.model.County;
import com.ypb.coolweather.model.Province;
import com.ypb.coolweather.tools.inter.GenArea;

public class GenAreaImpl implements GenArea<Area> {

    private AreaLevel arealevel = AreaLevel.PROVINCE;

    public GenAreaImpl(){
    }

    public void setLevel(AreaLevel areaLevel)
    {
        this.arealevel = areaLevel;
    }

    @Override
    public Area genNext() {
        if( arealevel == AreaLevel.PROVINCE )
            return new Province();
        else if(arealevel == AreaLevel.CITY)
            return new City();
        else if(arealevel == AreaLevel.COUNTY)
            return new County();
        else return new Province();
    }

    @Override
    public Area genNext(String name, String tag, String superTag) {
        Area area ;
        if( arealevel == AreaLevel.PROVINCE )
            area = new Province();
        else if(arealevel == AreaLevel.CITY)
            area = new City();
        else if(arealevel == AreaLevel.COUNTY)
            area = new County();
        else area = new Province();
        area.setName(name);
        area.setTag(tag);
        area.setSuperTag(superTag);
        return area;
    }
}
