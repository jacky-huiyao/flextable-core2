package com.lianfan.flextable;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FlexCell {
    private String text;
    private Integer rowSpan;
    private Integer colSpan;
    private Map<String,Object> properties;

    public FlexCell(String text) {
        this.text=text;
        this.rowSpan = 1;
        this.colSpan = 1;
    }

    public FlexCell(String text, @NotNull Integer rowSpan, @NotNull Integer colSpan, @NotNull Map<String, Object> properties) {
        this.text = text;
        this.rowSpan = rowSpan;
        this.colSpan = colSpan;
        this.properties = properties;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(@NotNull Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(@NotNull Integer colSpan) {
        this.colSpan = colSpan;
    }

    public void addProperty(Map<String,Object> properties){
        Objects.requireNonNull(properties);
        if(this.properties==null){
            this.properties = new HashMap<>();
        }
        this.properties.putAll(properties);
    }
}
