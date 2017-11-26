package com.lianfan.flextable.core;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class FlexColumn<P, T, C, R> implements Comparable<FlexColumn>{
    private String key;
    private String title;
    private Integer colSpan=1;
    private Integer rowSpan=1;
    private String dataIndex;
    private Integer colOff;
    private Integer rowOff;
    private Integer order=1;
    private Integer fixed=1;
    public Map<String,Object> properties;
    protected Function<? super P,? extends T> mapper;
    protected Function<R,? extends Serializable> render;
    List<FlexColumn<T, C,?,?>> children;
    FlexColumn<?, P, T,?> parent;
    Consumer<FlexContext> postProcess;

    public FlexColumn(String title, String dataIndex, Function<? super P, ? extends T> mapper, Function<R, ? extends Serializable> render) {
        this.key = UUID.randomUUID().toString();
        this.title = title;
        this.dataIndex = dataIndex;
        this.mapper = mapper;
        this.render = render;
    }

    /**
     * 初始化准备工作，例如设置children,rowspan等
     * @param context 表格上下文
     * @param data 实际数据
     */
    public void doPreProcess(FlexContext context, List<?> data){

    }

    public void doInitContext(FlexContext context){
        doPreProcess(context,context.getData());
        context.addColumn(this,parent);
    }

    public <F> void doInitCell(FlexContext context){
        List<FlexRow<F>> instRows = context.getInstRows();
        Integer maxColSpan=0;
        for(FlexRow<F> row:instRows){
            FlexCell cell = new FlexCell(doMapper(row.getRecord().get(0)),render);
            cell.doInitContext(context,this,row);
            if(cell.getColSpan()>maxColSpan){
                maxColSpan = cell.getColSpan();
            }
        }
        colSpan=maxColSpan;
        if(maxColSpan>1) {
            instRows.forEach(row -> context.getCell(row.getKey() + ":" + key).setColSpan(getColSpan()));
        }
    }

    public void doRender(FlexContext context){
        if(children!=null){
            children.forEach(col->{
                doRender(context);
            });
            children.stream().mapToInt(FlexColumn::getColSpan).max().ifPresent(max->setColSpan(max));
        }
    }

    public void doPostProcess(FlexContext context){
        if(postProcess!=null){
            postProcess.accept(context);
        }
    }

    public <F> T doMapper(F t){
       if(parent!=null){
            return mapper.apply(parent.doMapper(t));
       }
       return mapper.apply((P)t);
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Fixed getFixed() {
        return Fixed.valueOf(fixed);
    }

    public void setFixed(Fixed fixed) {
        this.fixed = fixed.ordinal();
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Function<? super P, ? extends T> getMapper() {
        return mapper;
    }

    public void setMapper(Function<? super P, ? extends T> mapper) {
        this.mapper = mapper;
    }

    public Function<?, ? extends Serializable> getRender() {
        return render;
    }

    public void setRender(Function<R, ? extends Serializable> render) {
        this.render = render;
    }

    public List<FlexColumn<T, C, ?,?>> getChildren() {
        if(children==null){
            children = new LinkedList<>();
        }
        return children;
    }

    public void setChildren(List<FlexColumn<T, C, ?,?>> children) {
        this.children = children;
    }

    public FlexColumn<?, P, T,?> getParent() {
        return parent;
    }

    public void setParent(FlexColumn<?, P, T,?> parent) {
        this.parent = parent;
    }

    public void setPostProcess(Consumer<FlexContext> postProcess) {
        this.postProcess = postProcess;
    }

    public Integer getColOff() {
        return colOff;
    }

    public void setColOff(Integer colOff) {
        this.colOff = colOff;
    }

    public Integer getRowOff() {
        return rowOff;
    }

    public void setRowOff(Integer rowOff) {
        this.rowOff = rowOff;
    }

    @Override
    public int compareTo(FlexColumn o) {
        if(!Objects.equals(this.fixed, o.fixed)){
            return this.fixed-o.fixed;
        }
        return this.order-o.order;
    }


    public enum Fixed{
        LEFT,
        NONE,
        RIGHT;

        public static Fixed valueOf(int value) {
            switch (value) {
                case 0:
                    return LEFT;
                case 1:
                    return NONE;
                case 2:
                    return RIGHT;
                default:
                    return null;
            }
        }
    }
}
