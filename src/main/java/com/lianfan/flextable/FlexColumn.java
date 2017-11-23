package com.lianfan.flextable;


import com.sun.istack.internal.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * 表格列
 * @param <T>
 * @param <R>
 */
public class FlexColumn<T,R> implements Comparable<FlexColumn<T,R>>{
    /**
     * 全局唯一标识符
     */
    private String key;
    /**
     * 列索引
     */
    private String dataIndex;
    /**
     * 列标题
     */
    private String title;
    /**
     * 列号
     */
    private Integer colNo;
    /**
     * 行号
     */
    private Integer rowNo;
    /**
     * 列跨度
     */
    private Integer colSpan;
    /**
     * 行跨度
     */
    private Integer rowSpan;
    /**
     * 列排序
     */
    private Integer order;

    private Integer fixed;

    /**
     * 状态，表示是否可以对该列进行操作（如merge,split）
     */
    Boolean active;


    /**
     * 对象映射关系
     */
    protected Function<? super T,? extends R> mapper;
    /**
     * 值渲染
     */
    protected Function<R,? extends Serializable> render;

    private List<FlexColumn> children;

    /**
     * 列属性
     */
    public Map<String,Object> properties;

    public FlexColumn(@NotNull String dataIndex,@NotNull String title,@NotNull Integer order,Function<? super T, ? extends R> mapper,Function<R, ? extends Serializable> render,List<FlexColumn> children,@NotNull Map<String, Object> properties) {
        this.dataIndex = dataIndex;
        this.title = title;
        this.mapper = mapper;this.order = order;
        this.render = render;
        this.children = children;
        this.properties = properties;
        this.colSpan = 1;
        this.rowSpan = 1;
        this.active = true;
        this.fixed = Fixed.NONE.value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getDataIndex() {
        return dataIndex;
    }

    public String getTitle() {
        return title;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public Integer getOrder() {
        return order;
    }

    public void addProperty(Map<String,Object> properties){
        this.properties.putAll(properties);
    }

    public Boolean containsProperty(String key){
        return properties.containsKey(key);
    }

    public Object getProperty(String key){
        return properties.get(key);
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<FlexColumn> getChildren() {
        return children;
    }

    public void setFixed(Fixed fixed){
        this.fixed=fixed.value;
    }

    public Fixed getFixed(){
        return Fixed.valueOf(fixed);
    }


    public Integer getColNo() {
        return colNo;
    }

    public void setColNo(Integer colNo) {
        this.colNo = colNo;
    }

    public Integer getRowNo() {
        return rowNo;
    }

    public void setRowNo(Integer rowNo) {
        this.rowNo = rowNo;
    }

    @Override
    public int compareTo(FlexColumn<T, R> other) {
        return this.order-other.order;
    }

    /**
     * 初始化表格上下文，将表格与列建立关联
     * @param context 表格上下文
     * @param parent 父级列
     */
    public void doInitContext(FlexContext context,FlexColumn<T,?> parent){
        if(key==null) {
            context.createColumn(parent.getKey() != null ? parent.getKey() : null, this);
        }else {
            context.replaceColumn(parent.getKey() != null ? parent.getKey() : null, this);
        }
    }

    /**
     * 表格渲染时调用
     * @param context 表格上下文
     * @param r 映射对象
     * @return 渲染后的值
     */
    public String doRender(FlexContext context,R r) {
        Objects.requireNonNull(r,"渲染"+title+"列时传入了空的对象");
        if(render==null){
            return r.toString();
        }
        return render.apply(r).toString();
    }

    /**
     * 列实例化时调用
     * @param context 表格上下文
     * @param t 实例对象
     * @return
     */
    public R doMapper(FlexContext context,T t){
        Objects.requireNonNull(t,"渲染"+title+"列时传入了空的对象");
        Objects.requireNonNull(mapper);
        if(mapper!=null){
            return mapper.apply(t);
        }
        return null;
    }

    public enum Fixed{
        LEFT(0),
        NONE(1),
        RIGHT(2);

        private int value = 0;

        Fixed(int value) {
            this.value = value;
        }

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
