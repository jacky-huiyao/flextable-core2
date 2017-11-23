package com.lianfan.flextable;

import com.sun.istack.internal.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * user: zhangyaohui
 * date: 2017/11/23
 *
 * @param <T> the type parameter
 */
public class FlexContext<T> {

    private Integer x=0;
    private Integer y=0;
    private Integer colWidth;
    private Integer colHeight;
    private List<FlexCell> colCells;
    /**
     * 维护所有列
     */
    private Map<String,FlexColumn<T,?>> colMap = new HashMap<>();
    /**
     * 维护所有列的层次关系
     */
    private Map<String,List<String>> colTagMap = new HashMap<>();
    /**
     * create by renderColumn function
     */
    private Map<String,FlexCell> dataCellMap;
    private Map<String,List<String>> rowTagMap = new HashMap<>();
    private Map<String,FlexRow<T>> rowMap = new HashMap<>();


    public final String delimiter = ":";

    public FlexContext() {
        colTagMap.put("root", new LinkedList<>());
    }

    /**
     * 设置表格的位置
     * @param x 左上角x坐标,水平
     * @param y 左上角y坐标,垂直
     */
    public void setPosition(Integer x,Integer y){
        this.x=x;
        this.y=y;
    }

    /**
     * 创建列
     * @param parentKey 所属父列的key
     * @param flexColumn 创建列
     * @param <R> 映射类型
     * @return 带key的FlexColumn
     */
    public <R> FlexColumn<T,R> createColumn(String parentKey,FlexColumn<T,R> flexColumn){
        String key;
        if(parentKey == null){
            key = "root"+delimiter+colTagMap.get("root").size();
            colTagMap.get("root").add(key);
        } else {
            if(colTagMap.get(parentKey)==null){
                colTagMap.put(parentKey,new LinkedList<>());
            }
            key = parentKey+delimiter+colTagMap.get(parentKey).size();
            colTagMap.get(parentKey).add(key);
        }
        colMap.put(key,flexColumn);
        if(flexColumn.getDataIndex()!=null){
            colMap.put(flexColumn.getDataIndex(),flexColumn);
        }
        if(flexColumn.getChildren()!=null){
            flexColumn.getChildren().forEach(column -> createColumn(key,column));
        }
        return flexColumn;
    }

    /**
     * @param key 替换列的key
     * @param replaceColumn 替换列
     * @param <R>
     * @return 带key的列
     */
    public <R> FlexColumn replaceColumn(@NotNull String key,FlexColumn<T,R> replaceColumn){
        if(colTagMap.get(key)!=null) {
            String parentKey = getParentKey(key);
            if (parentKey != null) {
                deleteColumn(parentKey, key);
                return createColumn(parentKey, replaceColumn);
            }
        }
        return null;
    }

    public void deleteColumn(@NotNull String key){
        String parentKey = getParentKey(key);
        if (parentKey != null) {
            deleteColumn(parentKey, key);
        }
    }

    public void deleteColumn(String parentKey,String key){
        if(colTagMap.get(parentKey)!=null){
            for (String k:colTagMap.get(parentKey)){
                if(k.equals(key)){
                    colTagMap.get(parentKey).remove(k);
                    break;
                }
            }
        }
    }

    public String getParentKey(String key){
        if(key.lastIndexOf(delimiter)!=-1) {
            return key.substring(0, key.lastIndexOf(delimiter));
        } else{
            return null;
        }
    }

    public FlexColumn<T,?> getColumn(String key){
        if(colMap.containsKey(key)){
            return colMap.get(key);
        }
        return null;
    }

    public List<FlexColumn<T,?>> getColumnByParentKey(String parentKey){
        if(colTagMap.containsKey(parentKey)){
            return colTagMap.get(parentKey).stream().map(key->getColumn(key)).collect(Collectors.toList());
        }
        return null;
    }

    public List<FlexColumn<T,?>> getBottomColumnByKey(String key){
        if(colTagMap.containsKey(key) && colTagMap.get(key).size()>0){
            List<FlexColumn<T,?>> result = new ArrayList<>();
            colTagMap.get(key).stream().forEach(k->{
                result.addAll(getBottomColumnByKey(k));
            });
            return result;
        }
        if(colMap.get(key)!=null && colMap.get(key).getRowSpan()>0){
            return Arrays.asList(colMap.get(key));
        }
        return Arrays.asList();
    }

    public List<FlexColumn<T,?>> getRootColumns(){
        return colTagMap.get("root").stream().map(key->getColumn(key)).collect(Collectors.toList());
    }

    public List<FlexColumn<T,?>> getBottomColumns(){
        return getBottomColumnByKey("root");
    }

    public void sortColumn(String key,Boolean enableFixed){
        if(colTagMap.get(key)!=null){
            if(enableFixed) {
                colTagMap.get(key).sort((c1, c2) -> {
                    FlexColumn o1 = colMap.get(c1);
                    FlexColumn o2 = colMap.get(c2);
                    if (o1.getFixed() == o2.getFixed()) {
                        return o1.getOrder() - o2.getOrder();
                    }
                    return o1.getFixed().ordinal() - o2.getFixed().ordinal();
                });
            }else{
                Collections.sort(colTagMap.get(key));
            }
            colTagMap.get(key).forEach(k-> sortColumn(k,enableFixed));
        }
    }

    public void renderColumn(int x,int y){
        colWidth=0;
        colHeight=0;
        renderColumnColSpan();
        renderColCell(getRootColumns(),x,y);
    }

    public void renderColumnColSpan(){
        colTagMap.get("root").forEach(k->{
            final FlexColumn<T,?> column = colMap.get(k);
            calculateColSpanAndColHeight(column,column.getRowSpan());
        });
    }

    private Integer calculateColSpanAndColHeight(FlexColumn<T,?> column,Integer deep){
        if(column.getChildren()!=null){
            int cspan=0;
            for(FlexColumn col:column.getChildren()){
                cspan+=calculateColSpanAndColHeight(col,deep+col.getRowSpan());
            }
            column.setColSpan(cspan);
            return cspan;
        }
        if(deep>colHeight){
            colHeight=deep;
        }
        return column.getColSpan();
    }

    private void renderColCell(List<FlexColumn<T,?>> columns,Integer x,Integer y){
        int colNo=x;
        for(FlexColumn<T,?> column:columns){
            if(column.getRowSpan()>0 && column.getColSpan()>0) {
                FlexCell cell = new FlexCell(column.getTitle(), column.getRowSpan(), column.getColSpan(), column.getProperties());
                cell.setColNo(colNo);
                cell.setRowNo(y);
                if(column.getChildren()==null){
                    cell.setRowSpan(this.y+colHeight-y);
                }
                colCells.add(cell);
                colNo+=column.getColSpan();
            }
            if(column.getChildren()!=null){
                renderColCell(column.getChildren(),x,y+column.getRowSpan());
            }
        }
    }

}
