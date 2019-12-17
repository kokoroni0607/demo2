package com.credit.demo2.enums;


/**
 * \* User: Sanm
 * \* Date: 2019/5/4
 */
public enum LoginEnum {

    ENABLE(0, "正常"),
    LOCK(1, "帐号锁定");

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    LoginEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}