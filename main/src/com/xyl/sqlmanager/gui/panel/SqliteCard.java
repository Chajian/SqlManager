package com.xyl.sqlmanager.gui.panel;

import com.xyl.sqlmanager.gui.DBFileFilter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SqliteCard extends Panel {
    JButton openSqliteFile;
    File sqliteFile = null;

    {
        //sqlite
        openSqliteFile = new JButton("选择sqlite文件");
        openSqliteFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfilechooser = new JFileChooser();
                FileFilter filter = new DBFileFilter();
                jfilechooser.setFileFilter(filter);
                int returnVal = jfilechooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    sqliteFile = jfilechooser.getSelectedFile();
                }
            }
        });
        add(new JLabel("选择Sqlite的db文件"));
        add(openSqliteFile);
        setVisible(true);
    }

    public File getSqliteFile() {
        return sqliteFile;
    }

    public void setSqliteFile(File sqliteFile) {
        this.sqliteFile = sqliteFile;
    }
}