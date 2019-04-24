package com.ypb.coolweather.tools.inter;

public interface GenArea<T> {
    T genNext();
    T genNext(String name,String tag,String superTag);
}
