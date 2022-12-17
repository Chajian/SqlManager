package com.xyl.sqlmanager.util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;

/**
 * 表格处理
 */
public class TableHandler {


    /**
     * @param model 表模版
     * @param columns 列名
     * @param data 数据
     */
    public static void updateTableColumn(JTable table, DefaultTableModel model, List<String> columns, List<Map<String,String>> data){
        Object[][] datas = toDoubleDimension(columns,data);
        model.setDataVector(datas,columns.toArray());
        table.revalidate();
    }
    /**
     * @param model 表模版
     * @param data 数据
     */
    public static void updateTableColumn(JTable table, DefaultTableModel model, List<Map<String,String>> data){
        if(data.size()>0) {
            String[] data0 = data.get(0).keySet().toArray(new String[]{});
            List<String> columns = List.of(data0);

            Object[][] datas = toDoubleDimension(columns, data);
            model.setDataVector(datas, columns.toArray());
            table.revalidate();
        }
    }


    /**
     * 把List<Map>转二维数组，并且保证数据一致性，和有序性
     * @params columns 列名
     * @param data 表格数据
     * @return
     */
    public static Object[][] toDoubleDimension(List<String> columns,List<Map<String,String>> data){
        Object[][] datas = new Object[data.size()][columns.size()];

        var ref = new Object() {
            int on = 0;
        };
        data.stream()
                .forEach(e1->{
                    Object[] row = new Object[columns.size()];
                    for(int i = 0 ;i < columns.size() ;i++){
                        row[i] = e1.get(columns.get(i));
                    }
                    datas[ref.on] = row;
                    ref.on+=1;
                });
        return datas;
    }

    public static void deleteRow(JTable jTable,DefaultTableModel model,int row){
        model.removeRow(row);
        jTable.revalidate();
    }

    public static void insertRow(JTable jTable,DefaultTableModel model,Object[] row){
        int nowRow = model.getRowCount();
        model.addRow(row);
        jTable.revalidate();
        jTable.setRowSelectionInterval(nowRow, nowRow);
    }

    public static List<String> getRowData(JTable jTable,DefaultTableModel model,int row){
        if(row>=jTable.getRowCount()){
            return null;
        }
        List<String> data = new ArrayList<>();
        int columnCount = jTable.getColumnCount();
        for(int i = 0 ; i < columnCount ; i++){
            data.add((String) model.getValueAt(row,i));
        }
        return data;
    }

}
