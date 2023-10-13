package com.xyl.sqlmanager.exception;

public enum ResponseEnum {
    USE_DATA_EXCEPTION("EX501","使用数据库时出现异常"),
    SELECT_EXCEPTION("EX502","select语句出问题"),
    DES_EXCEPTION("EX503","desc语句出问题"),
    CUS_SQL_EXCEPTION("EX505","执行自定义sql出问题"),

    DRIVE_SQL_EXCEPTION("EX506","执行mysql驱动出问题"),
    ;


    private String code;
    private String mes;

    ResponseEnum(String code, String mes) {
        this.code = code;
        this.mes = mes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
}
