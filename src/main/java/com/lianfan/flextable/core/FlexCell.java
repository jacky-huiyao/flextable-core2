package com.lianfan.flextable.core;

import com.sun.istack.internal.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class FlexCell<R> {
    private String text;
    private Integer rowSpan;
    private Integer colSpan;
    private Integer rowOffset;
    private Integer colOffset;
    private Map<String,Object> properties;
    FlexCell left;
    FlexCell down;
    Function<R,? extends Serializable> render;
    R r;

    public FlexCell(R r,Function<R,? extends Serializable> render) {
        this.render = render;
        this.r = r;
    }

    public <T> void doInitContext(FlexContext context,FlexColumn<T,R,?,?> column,FlexRow<T> row){
        context.addCell(this,row.getKey(),column.getKey());
    }

    public void render(){
        text = render.apply(r).toString();
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

    public Integer getRowOffset() {
        return rowOffset;
    }

    public void setRowOffset(Integer rowOffset) {
        this.rowOffset = rowOffset;
    }

    public Integer getColOffset() {
        return colOffset;
    }

    public void setColOffset(Integer colOffset) {
        this.colOffset = colOffset;
    }

    public FlexCell getLeft() {
        return left;
    }

    public void setLeft(FlexCell left) {
        this.left = left;
    }

    public FlexCell getDown() {
        return down;
    }

    public void setDown(FlexCell down) {
        this.down = down;
    }


}
