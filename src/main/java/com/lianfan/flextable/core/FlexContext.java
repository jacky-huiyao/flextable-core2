package com.lianfan.flextable.core;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FlexContext<T> {
    private Map<String,FlexColumn> colMap;
    private Map<String,FlexRow<T>> rowMap;
    private Map<String,FlexCell> cellMap;
    private Map<String,List<String>> colRowMap;
    private List<T> data;
    private List<FlexColumn<?,T,?,?>> columns;

    public void addColumn(FlexColumn column,FlexColumn parent){
        if(column.getDataIndex()!=null){
            colMap.put(column.getDataIndex(),column);
            if(!column.getChildren().isEmpty()){
                List<FlexColumn> children = column.getChildren();
                children.forEach(col->{
                    col.setParent(column.getRowSpan()==0?parent:column);
                    col.doInitContext(this);
                });
            }
            if(parent!=null){
                parent.getChildren().add(column);
            }
        }
    }

    public void addRow(FlexRow<T> row,FlexRow<T> parent){
        rowMap.put(row.getKey(),row);
        if(!row.getChildren().isEmpty()){
            row.getChildren().forEach(r->r.setParent(row));
        }
        if(parent!=null){
            parent.getChildren().add(row);
        }
    }

    public void addCell(FlexCell cell,String rowKey,String colKey){
        cellMap.put(rowKey+":"+colKey,cell);
    }

    public void initColumns(){
        clear();
        FlexColumn root = new FlexColumn<>("root", "root", Function.identity(), Function.identity());
        root.setRowSpan(0);
        root.setChildren(columns);
        root.doInitContext(this);
        initCells();
    }

    public void initRows(){
        rowMap = new HashMap<>(data.size());
        FlexRow root = new FlexRow(data, FlexRow.RowType.GROUP);
        data.forEach(d->{
            FlexRow<T> instRow = new FlexRow<>(Arrays.asList(d), FlexRow.RowType.INST);
            instRow.doInitContext(this);
        });
        root.setColSpan(0);
        root.setChildren(new ArrayList<>(rowMap.values()));
        rowMap.put("root",root);
    }

    public void initCells(){
        List<FlexColumn> bottomColumns = getInstColumns();
        bottomColumns.forEach(col->{
            col.doInitCell(this);
        });
    }

    public static void sortCol(FlexColumn column) {
        if(column.getChildren().size()>1){
            Collections.sort(column.getChildren());
        }
    }

    public <R> void sortRow(String dataIndex,Comparator<R> comparator) {
        FlexColumn<?,R,?,?> flexColumn = colMap.get(dataIndex);
        if(flexColumn==null || flexColumn.getChildren()!=null) return;
        if(!colRowMap.isEmpty()){
            FlexRow<T> tail = rowMap.get("root").getChildren().get(0).getParent();
            if(tail!=null){
                colRowMap.get(tail.getKey()).forEach(key->{
                      Collections.sort(rowMap.get(key).getChildren(),(r1,r2)-> comparator.compare(flexColumn.doMapper(r1.getRecord().get(0)),flexColumn.doMapper(r2.getRecord().get(0))));
                });
                return;
            }
        }
        Collections.sort(rowMap.get("root").getChildren(),(r1,r2)->comparator.compare(flexColumn.doMapper(r1.getRecord().get(0)),flexColumn.doMapper(r2.getRecord().get(0))));
    }

    public void doMerge(){

    }

    public FlexColumn getColumn(String dataIndex){
        return colMap.get(dataIndex);
    }

    public FlexCell getCell(String key){
        return cellMap.get(key);
    }

    public List<FlexColumn> getInstColumns(){
        FlexColumn<T,?,?,?> root = colMap.get("root");
        Objects.requireNonNull(root);
        List<FlexColumn> result = new ArrayList<>(colMap.size());
        root.children.forEach(col->{
            if(col.getChildren().isEmpty()){
                result.add(col);
            }
        });
        return result;
    }

    public List<FlexRow<T>> getInstRows(){
        FlexRow<T> root = rowMap.get("root");
        Objects.requireNonNull(root);
        return root.getChildren();
    }

    public void clear(){
        colMap = new HashMap<>();
        cellMap = new HashMap<>();
        colRowMap = new HashMap<>();
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<FlexColumn<?, T, ?,?>> getColumns() {
        return columns;
    }

    public void setColumns(List<FlexColumn<?, T, ?,?>> columns) {
        this.columns = columns;
    }

    public Integer getColumnMaxDeep(){
        FlexColumn<?,T,?,?> root = getColumn("root");
        return getDeep(root);
    }

    private Integer getDeep(FlexColumn<?,?,?,?> column){
        Integer deep=0;
        if(column.getChildren()!=null){
            for (FlexColumn col : column.getChildren()) {
                Integer deep1 = getDeep(col);
                if (deep1 > deep) {
                    deep = deep1;
                }
            }
        }
        return deep+column.getRowSpan();
    }


}
