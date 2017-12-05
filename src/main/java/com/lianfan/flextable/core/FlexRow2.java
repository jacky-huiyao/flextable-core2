package com.lianfan.flextable.core;

import java.util.List;

public abstract class FlexRow2<T> {
    private String rowIndex;
    private Integer offset;
    private Integer span;
    private List<Row<T>> records;

    public abstract void doRender(FlexContext<T> context);

    public abstract void doMerge(FlexContext<T> context,String dataIndex);

    public abstract void doSort(FlexContext<T> context,String dataIndex);

    public abstract List<Row<T>> getRecords();

    public abstract Integer getSpan();

    public String getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(String rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public void setSpan(Integer span) {
        this.span = span;
    }

    public void setRecords(List<Row<T>> records) {
        this.records = records;
    }
}
