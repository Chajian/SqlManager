package com.xyl.sqlmanager.gui.main;

import com.xyl.sqlmanager.SqlManagerContext;
import com.xyl.sqlmanager.util.TableHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

public class TableCard extends Panel implements MouseListener {
    private int seletedRow = -1,seletedCol = -1;
    private Object value;
    //是否插入
    boolean isInsert = false;
    boolean isEnable = true;
    JTable table;
    DefaultTableModel tableModel;
    JScrollPane spTable;



    @Override
    public void mouseClicked(MouseEvent e) {
        if(isEnable) {
            // 获取点击位置的行和列索引
            seletedRow = table.rowAtPoint(e.getPoint());
            seletedCol = table.columnAtPoint(e.getPoint());
            // 根据行和列索引获取单元格的值
            value = table.getValueAt(seletedRow, seletedCol);

            if (isInsert) {
                int newRow = table.getRowCount() - 1;
                if (newRow != seletedRow && checkRowHasNull(newRow)) {
                    JOptionPane.showMessageDialog(null, "插入数据必须全部填写完整!");
                    TableHandler.deleteRow(table, tableModel, newRow);
                    isInsert = false;
                } else if (newRow != seletedRow) {
                    try {
                        MainPanel mainPanel = (MainPanel) SqlManagerContext.getSqlManagerContext().getCurFrame();
                        mainPanel.getMySqlDriver().insert(mainPanel.getConnection(), mainPanel.getSeletedTable(), TableHandler.getRowData(table, tableModel, newRow));

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "插入数据成功!");
                    isInsert = false;
                }
            }
        }
    }

    /**
     * 检车row行是否有空
     * @param row
     * @return
     */
    public boolean checkRowHasNull(int row){
        int columnSize = table.getColumnCount();
        for(int i = 0 ;i < columnSize; i++){
            String value = (String) table.getValueAt(row,i);
            if(value==null)
                return true;
        }
        return false;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public int getSeletedRow() {
        return seletedRow;
    }

    public int getSeletedCol() {
        return seletedCol;
    }

    public Object getValue() {
        return value;
    }


    public TableCard(){
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        // 设置表格选择模式为单一选择
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // 创建一个滚动面板，包含表格
        spTable = new JScrollPane(table);
        //table添加鼠标事件
        table.addMouseListener(this);
    }

}