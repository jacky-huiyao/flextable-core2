package com.lianfan.flextable;

import java.util.List;
import java.util.Map;

/**
 * user: zhangyaohui
 * date: 2017/11/23
 */
public class FlexTable<T> {
    private String title;
    private Map<String,Object> properties;
    private FlexContext<T> context = new FlexContext<>();


    public FlexTable() {
    }

    public void setColumns(List<FlexColumn<T,?>> columns){

    }

    public void setData(List<T> data){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public FlexContext<T> getContext() {
        return context;
    }

}
