package com.lianfan.flextable.core;

import java.util.Map;

public class Row<T> {
    private String key;
    private Integer offset;
    private Integer span;
    private T reocrd;
    private Map<String,Object> properties;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getSpan() {
        return span;
    }

    public void setSpan(Integer span) {
        this.span = span;
    }

    public T getReocrd() {
        return reocrd;
    }

    public void setReocrd(T reocrd) {
        this.reocrd = reocrd;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
