package com.lianfan.flextable.core;

import com.lianfan.flextable.support.DateColumn;
import com.lianfan.flextable.support.SimpleColumn;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class FlexTableTest {

    FlexTable table;

    @Before
    public void before(){
        table = new FlexTable<User>();
    }

    @Test
    public void setData() throws Exception {
        List<User> users=new ArrayList<>();
        for(int i=0;i<5;i++){
            users.add(new User(i,new Random().nextInt(20)+10,"name"+i,"addr"+i,new Date()));
        }
        table.setData(users);
    }

    @Test
    public void setColumns() throws Exception {
        setData();
        table.setColumns(new SimpleColumn<>("姓名","name",User::getName),
                         new DateColumn<>("生日","birthday",User::getBirthday));
    }

    @Test
    public void render() throws Exception {
        setColumns();
        table.render();
        System.out.println(table);
    }


    @Test
    public void sort() throws Exception {
        setColumns();
        table.sortCol();
    }

    @Test
    public void sortRow()throws Exception {
        setColumns();
        table.sortRow("birthday", (Comparator<Date>) Date::compareTo);
    }
}