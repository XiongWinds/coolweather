package com.ypb.coolweather.model;

public class Weather {
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTempLow(String tempLow) {
        this.tempLow = tempLow;
    }

    public void setTempHigh(String tempHigh) {
        this.tempHigh = tempHigh;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    private String desc;
    private String tempLow;
    private String tempHigh;
    private String pubTime;

    public String getDesc() {
        return desc;
    }

    public String getTempLow() {
        return tempLow;
    }

    public String getTempHigh() {
        return tempHigh;
    }

    public String getPubTime() {
        return pubTime;
    }
}
