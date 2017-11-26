package com.lianfan.flextable.support;

import com.lianfan.flextable.core.FlexColumn;

import java.io.Serializable;
import java.util.function.Function;

public class SimpleColumn<T> extends FlexColumn<T,Serializable,String,T> {
    public SimpleColumn(String title, String dataIndex, Function<? super T, ? extends Serializable> mapper) {
        super(title, dataIndex, mapper, Object::toString);
    }
}
