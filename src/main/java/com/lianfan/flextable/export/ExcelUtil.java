package com.lianfan.flextable.export;

import com.lianfan.flextable.core.*;

public class ExcelUtil<T> implements Exportor {

    FlexTable<T> table;
    FlexContext<T> context;
    Integer maxDeep;

    public ExcelUtil(FlexTable<T> table) {
        this.table = table;
        this.context = table.getContext();
    }

    @Override
    public void export() {

    }

    private void applyRowSpan(){
        maxDeep = context.getColumnMaxDeep();
        applyOffset(context.getColumn("root"),0,0);
    }

    private void applyOffset(FlexColumn<?,T,?,?> column,Integer colOffset,Integer rowOffset){
        Integer subOffset=0;
        if(column.getChildren()!=null) {
            for (FlexColumn col : column.getChildren()) {
                applyOffset(col,colOffset+subOffset,rowOffset+column.getRowSpan());
                subOffset+=col.getColSpan();
            }
        }else{
            column.setColOff(colOffset);
            column.setRowOff(rowOffset);
            column.setRowSpan(maxDeep-rowOffset);
            Integer rowCount=0;
            for (FlexRow<T> row : context.getInstRows()) {
                if(row.getTitle()!=null) {
                    for (FlexRow<T> trow : row.getTitle()) {
                        String cellKey=trow.getKey() + ":" + column.getKey();
                        if(context.getCell(cellKey)!=null){
                            FlexCell cell = context.getCell(cellKey);
                            cell.setColOffset(colOffset);
                            cell.setRowOffset(rowCount);
                        }
                        rowCount+=trow.getRowSpan();
                    }
                }
                for(FlexRow crow:row.getChildren()){
                    String CellKey=crow.getKey() + ":" + column.getKey();
                    if(context.getCell(CellKey)!=null){
                        FlexCell cell = context.getCell(CellKey);
                        cell.setColOffset(colOffset);
                        cell.setRowOffset(rowCount);
                    }
                    rowCount+=crow.getRowSpan();
                }
                if(row.getSummary()!=null) {
                    for (FlexRow<T> srow : row.getSummary()) {
                        String cellKey=srow.getKey() + ":" + column.getKey();
                        if(context.getCell(cellKey)!=null){
                            FlexCell cell = context.getCell(cellKey);
                            cell.setColOffset(colOffset);
                            cell.setRowOffset(rowCount);
                        }
                        rowCount+=srow.getRowSpan();
                    }
                }
            }

        }
    }




}
