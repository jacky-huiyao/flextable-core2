package com.lianfan.flextable.core;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FlexTable<T> {

    private FlexContext<T> context;

    public FlexTable() {
        context = new FlexContext<>();
    }

    public FlexTable setColumns(List<FlexColumn<?,T,?,?>> columns){
        context.setColumns(columns);
        if(context.getData()!=null){
            context.initColumns();
        }
        return this;
    }

    public FlexTable setColumns(FlexColumn<?,T,?,?>... columns){
        setColumns(Arrays.asList(columns));
        return this;
    }

    public FlexTable setData(List<T> data){
        context.setData(data);
        context.initRows();
        if(context.getColumns()!=null) {
            context.initColumns();
        }
        return this;
    }

    public FlexTable sortCol(){
        if(context.getColumns()!=null){
            context.sortCol(context.getColumn("root"));
        }
        return this;
    }

    public <R> FlexTable sortRow(String dataIndex,Comparator<R> comparator) {
        if(context.getData()!=null){
            context.sortRow(dataIndex,comparator);
        }
        return this;
    }

    public void render(){
        if(context.getData()!=null && context.getColumns()!=null){
            context.doMerge();
        }
    }

    public FlexContext<T> getContext() {
        return context;
    }
}
