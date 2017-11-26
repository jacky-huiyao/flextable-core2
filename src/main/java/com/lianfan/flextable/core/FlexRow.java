package com.lianfan.flextable.core;

import com.sun.istack.internal.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class FlexRow<T> {
    private String key;
    private Integer colSpan;
    private Integer rowSpan;
    private RowType type;
    FlexRow<T> parent;
    List<FlexRow<T>> children;
    List<FlexRow<T>> title;
    List<FlexRow<T>> summary;
    List<T> record;
    Consumer<FlexContext> postProcess;

    public FlexRow(@NotNull List<T> record,RowType type) {
        this.key= UUID.randomUUID().toString();
        this.record = record;
        this.type = type;
    }

    public void doPreProcess(FlexContext context, List<T> record){

    }

    public void doInitContext(FlexContext context){
        doPreProcess(context,record);
        context.addRow(this,parent);
    }

    public void doInitCell(FlexContext context){

    }

    public void doPostProcess(FlexContext context){
        if(postProcess!=null){
            postProcess.accept(context);
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public FlexRow<T> getParent() {
        return parent;
    }

    public void setParent(FlexRow<T> parent) {
        this.parent = parent;
    }

    public List<FlexRow<T>> getChildren() {
        if(children==null){
            return new LinkedList<>();
        }
        return children;
    }

    public void setChildren(List<FlexRow<T>> children) {
        this.children = children;
    }

    public void addChildren(FlexRow<T> child){
        getChildren().add(child);
    }

    public List<FlexRow<T>> getTitle() {
        return title;
    }

    public void setTitle(List<FlexRow<T>> title) {
        this.title = title;
    }

    public List<FlexRow<T>> getSummary() {
        return summary;
    }

    public void setSummary(List<FlexRow<T>> summary) {
        this.summary = summary;
    }

    public List<T> getRecord() {
        return record;
    }

    public void setRecord(List<T> record) {
        this.record = record;
    }

    public void setPostProcess(Consumer<FlexContext> postProcess) {
        this.postProcess = postProcess;
    }

    public RowType getType() {
        return type;
    }

    public enum RowType{
        HEAD,
        INST,
        FOOT,
        GROUP,
    }
}
