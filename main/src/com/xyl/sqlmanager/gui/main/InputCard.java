package com.xyl.sqlmanager.gui.main;

import com.xyl.sqlmanager.SqlManagerContext;
import com.xyl.sqlmanager.exception.CustomException;
import com.xyl.sqlmanager.exception.ResponseEnum;
import com.xyl.sqlmanager.util.TableHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class InputCard extends Panel {
    JTextArea sqlCommand;
    JButton send;
    public InputCard(){
        sqlCommand = new JTextArea();
        send = new JButton("执行sql指令");
        MainPanel mainPanel = (MainPanel) SqlManagerContext.getSqlManagerContext().getCurFrame();
        TableCard tableCard = mainPanel.getTableCard();
        MenuCard jMenuBar = mainPanel.getjMenuBar();
        this.setLayout(new GridLayout(1,2));
        this.add(sqlCommand);
        this.add(send);
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableCard.setEnable(false);//关闭表格监听
                jMenuBar.insert.setEnabled(false);
                jMenuBar.delete.setEnabled(false);
                try {
                    String sql = sqlCommand.getText().toString();
                    List<Map<String,String>> tempDatas = mainPanel.getMySqlDriver().executeSql(mainPanel.getConnection(),sql);
                    TableHandler.updateTableColumn(tableCard.table,tableCard.tableModel,tempDatas);
                } catch (SQLException ex) {
                    throw new CustomException(ResponseEnum.CUS_SQL_EXCEPTION.getCode(),ResponseEnum.CUS_SQL_EXCEPTION.getMes()+ex.getMessage());
                }
            }
        });
    }
}
