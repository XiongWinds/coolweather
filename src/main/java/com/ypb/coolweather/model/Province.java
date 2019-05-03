package com.ypb.coolweather.model;

public class Province extends Area{
    private String name = "";
    private String tag = "";
    private String superTag = "";

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public void setSuperTag(String superTag) {
        this.superTag = superTag;
    }

    @Override
    public String getSuperTag() {
        return superTag;
    }

    public String toString(){
        return this.name;
    }
}
