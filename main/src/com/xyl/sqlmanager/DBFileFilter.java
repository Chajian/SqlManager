package com.xyl.sqlmanager;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class DBFileFilter extends FileFilter {
    public boolean accept(File file) {
        // 如果文件是目录，则一定要接受
        if (file.isDirectory()) {
            return true;
        }

        // 如果文件是.db文件，则接受，否则拒绝
        String extension = getExtension(file);
        return extension != null && extension.equals("db");
    }

    public String getDescription() {
        return "SQLite数据库文件 (*.db)";
    }

    private String getExtension(File file) {
        String name = file.getName();
        int i = name.lastIndexOf('.');
        if (i > 0 && i < name.length() - 1) {
            return name.substring(i + 1).toLowerCase();
        }
        return null;
    }
}
