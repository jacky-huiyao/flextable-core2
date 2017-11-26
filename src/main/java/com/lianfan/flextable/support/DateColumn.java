package com.lianfan.flextable.support;

import com.lianfan.flextable.core.FlexColumn;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

public class DateColumn<T> extends FlexColumn<T,Date,String,T> {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public DateColumn(String title, String dataIndex, Function<? super T, ? extends Date> mapper) {
        super(title, dataIndex, mapper, date->sdf.format(date));
    }

    public DateColumn(String title, String dataIndex, Function<? super T, ? extends Date> mapper,String format) {
        super(title, dataIndex, mapper, date->sdf.format(date));
        if(format!=null) {
            sdf = new SimpleDateFormat(format);
        }
    }

}
