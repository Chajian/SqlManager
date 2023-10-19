package com.xyl.sqlmanager.gui.main;

import com.xyl.sqlmanager.SqlManagerContext;
import com.xyl.sqlmanager.util.TableHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuCard extends JMenuBar {
    JMenu operate;
    JMenuItem insert,delete;
    public MenuCard(){
        operate = new JMenu("操作");
        insert = new JMenuItem("插入");
        delete = new JMenuItem("删除");
        MainPanel mainPanel = (MainPanel) SqlManagerContext.getSqlManagerContext().getCurFrame();
        TableCard tableCard = mainPanel.getTableCard();
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tableCard.getSeletedRow();
                if(row>=0){

                    //数据库操作
                    Map<String,String> wholeRow = new HashMap<>();
                    List<Boolean> andOr = new ArrayList<>();
                    for(int i = 0 ; i < tableCard.tableModel.getColumnCount();i++){
                        wholeRow.put(tableCard.tableModel.getColumnName(i), (String) tableCard.tableModel.getValueAt(row,i));
                        andOr.add(true);
                    }
                    try {
                        mainPanel.getMySqlDriver().delete(mainPanel.getConnection(), mainPanel.getSeletedTable(),wholeRow,andOr);
                    } catch (SQLException ex) {
                        System.out.println("删除失败!");
                        ex.printStackTrace();
                    }
                    TableHandler.deleteRow(tableCard.table,tableCard.tableModel,row);
                }
            }
        });
        insert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mainPanel.getSeletedTable()!=null&&mainPanel.getSeletedDb()!=null){
                    TableHandler.insertRow(tableCard.table,tableCard.tableModel,null);
                    tableCard.isInsert = true;
                }
            }
        });
        operate.add(insert);
        operate.add(delete);
        add(operate);

    }

    public JMenuItem getInsert() {
        return insert;
    }

    public JMenuItem getDelete() {
        return delete;
    }
}
