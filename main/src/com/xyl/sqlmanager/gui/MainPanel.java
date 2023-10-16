package com.xyl.sqlmanager.gui;

import com.xyl.sqlmanager.BaseSqlDriver;
import com.xyl.sqlmanager.MySqlDriver;
import com.xyl.sqlmanager.SqlManagerContext;
import com.xyl.sqlmanager.exception.CustomException;
import com.xyl.sqlmanager.exception.ResponseEnum;
import com.xyl.sqlmanager.util.TableHandler;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class MainPanel extends JFrame implements TreeSelectionListener {

    /**
     * tree点击事件
     * @param e
     */
    @Override
    public void valueChanged(TreeSelectionEvent e)  {
        TreePath path = leftProject.getSelectionPath();
        if (path == null)
            return;
        if (path.getPathCount() == 2) {//点击数据库更新JTree组件ui
            DefaultMutableTreeNode treePath = (DefaultMutableTreeNode) path.getPath()[1];
            seletedDb = treePath.getUserObject().toString();
            mySqlDriver.useDataBase(connection, seletedDb);
        }
        if (path.getPathCount() == 3) {//点击表单更新JTableUI
            myMouseListener.setEnable(true);//开启表格监听
            insert.setEnabled(true);//启用插入功能
            delete.setEnabled(true);//启用删除功能
            DefaultMutableTreeNode treePath = (DefaultMutableTreeNode) path.getPath()[2];
            seletedTable = treePath.getUserObject().toString();
            List<String> columns = mySqlDriver.showTableColumnNames(connection,seletedTable);
            List<Map<String,String>> lists = mySqlDriver.select(connection,seletedTable,null,columns);
            //更新表格UI
            TableHandler.updateTableColumn(table,tableModel,columns,lists);
        }
    }



    JTree leftProject;
    DefaultTreeModel treeModel;
    DefaultMutableTreeNode root;
    String dbType = "Mysql";

    JTable table;
    DefaultTableModel tableModel;
    JScrollPane spTable;
    BaseSqlDriver mySqlDriver;
    Connection connection;
    MyMouseListener myMouseListener;

    JMenuBar jMenuBar;
    JMenu operate;
    JMenuItem insert,delete;
    String seletedDb,seletedTable;

    JTextArea sqlCommand;
    JButton send;


    public MainPanel(BaseSqlDriver mySqlDriver) throws HeadlessException, SQLException {
        // 创建根节点
        super("数据库");
        setLayout(new BorderLayout());
        this.mySqlDriver = mySqlDriver;

        //panel存左侧的树
        Panel panel = new Panel();
        root = new DefaultMutableTreeNode(dbType+"数据库");
        treeModel = new DefaultTreeModel(root);
        leftProject = new JTree(treeModel);
        leftProject.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        //获取所有数据库
        List<String> dbs = mySqlDriver.showDatabases(mySqlDriver.getConnection());
        //插入数据库node
        dbs.stream()
                .forEach(e->{
                    insertDbNode(e.toString());
                    //插入表单node
                    try {
                        connection = mySqlDriver.getConnection();
                        mySqlDriver.useDataBase(connection,e);
                        List<String> tables = mySqlDriver.showTables(connection);
                        tables.stream()
                                .forEach(e1->{
                                    insertTable(e.toString(),e1.toString());
                                });
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                });
        // 在面板的左侧放置树
        leftProject.addTreeSelectionListener(this);
        panel.add(new JScrollPane(leftProject));

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        // 设置表格选择模式为单一选择
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // 创建一个滚动面板，包含表格
        spTable = new JScrollPane(table);
        myMouseListener = new MyMouseListener();
        table.addMouseListener(myMouseListener);

        jMenuBar = new JMenuBar();
        operate = new JMenu("操作");
        insert = new JMenuItem("插入");
        delete = new JMenuItem("删除");

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = myMouseListener.getSeletedRow();
                if(row>=0){

                    //数据库操作
                    Map<String,String> wholeRow = new HashMap<>();
                    List<Boolean> andOr = new ArrayList<>();
                    for(int i = 0 ; i < tableModel.getColumnCount();i++){
                        wholeRow.put(tableModel.getColumnName(i), (String) tableModel.getValueAt(row,i));
                        andOr.add(true);
                    }
                    try {
                        mySqlDriver.delete(connection,seletedTable,wholeRow,andOr);
                    } catch (SQLException ex) {
                        System.out.println("删除失败!");
                        ex.printStackTrace();
                    }
                    TableHandler.deleteRow(table,tableModel,row);
                }
            }
        });
        insert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(seletedTable!=null&&seletedDb!=null){
                    TableHandler.insertRow(table,tableModel,null);
                    myMouseListener.isInsert = true;
                }
            }
        });
        operate.add(insert);
        operate.add(delete);
        jMenuBar.add(operate);

        JPanel panel1 = new JPanel();
        sqlCommand = new JTextArea();
        send = new JButton("执行sql指令");
        panel1.setLayout(new GridLayout(1,2));
        panel1.add(sqlCommand);
        panel1.add(send);
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myMouseListener.setEnable(false);//关闭表格监听
                insert.setEnabled(false);
                delete.setEnabled(false);
                try {
                    String sql = sqlCommand.getText().toString();
                    List<Map<String,String>> tempDatas = mySqlDriver.executeSql(connection,sql);
                    TableHandler.updateTableColumn(table,tableModel,tempDatas);
                } catch (SQLException ex) {
                    throw new CustomException(ResponseEnum.CUS_SQL_EXCEPTION.getCode(),ResponseEnum.CUS_SQL_EXCEPTION.getMes()+ex.getMessage());
                }
            }
        });

        //添加菜单栏
        setJMenuBar(jMenuBar);
        add(jMenuBar,BorderLayout.NORTH);
        add(panel,BorderLayout.WEST);
        // 将滚动面板添加到窗体中央
        this.add(spTable, BorderLayout.CENTER);
        add(panel1,BorderLayout.SOUTH);
        // 设定窗口大小
        this.setSize(1200, 400);
        // 设定窗口左上角坐标（X轴200像素，Y轴100像素）
        this.setLocation(300, 200);
        // 设定窗口默认关闭方式为退出应用程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗口可视（显示）
        this.setVisible(true);

        SqlManagerContext.getSqlManagerContext().setCurFrame(this);
    }


    /**
     * 插入库节点
     * @param dbName
     */
    public void insertDbNode(String dbName){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(dbName);
        treeModel.insertNodeInto(node,root,root.getChildCount());
    }

    /**
     * 插入表节点
     */
    public void insertTable(String dbName,String tableName){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(tableName);
        Enumeration<TreeNode> childrens = root.children();
        while(childrens.hasMoreElements()){
            DefaultMutableTreeNode node1 = (DefaultMutableTreeNode) childrens.nextElement();
            if(node1.getUserObject().equals(dbName)){
                treeModel.insertNodeInto(node,node1,node1.getChildCount());
            }
        }
    }

    class MyMouseListener implements MouseListener{
        private int seletedRow = -1,seletedCol = -1;
        private Object value;
        boolean isInsert = false;
        boolean isEnable = true;

        public MyMouseListener() {
        }

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
                            mySqlDriver.insert(connection, seletedTable, TableHandler.getRowData(table, tableModel, newRow));

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
    }
}
