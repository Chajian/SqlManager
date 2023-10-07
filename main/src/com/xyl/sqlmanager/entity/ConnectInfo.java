package com.xyl.sqlmanager.entity;

/**
 * 连接信息对象
 * 详细描述：
 * 每一个属性最长长度32字节
 * 每一个对象最大长度192字节
 * 超过最大长度的内容不会被保存
 */
public class ConnectInfo {
    String name;
    String host;
    String user;
    String pass;
    String port;
    String version;
    /**
     * 时间戳
     */
    String timestamp;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
