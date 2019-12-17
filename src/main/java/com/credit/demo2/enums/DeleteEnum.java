package com.credit.demo2.enums;


/**
 * \* User: Sanm
 * \* Date: 2019/5/4
 * \
 */
public enum DeleteEnum {

    EXIST(0,"存在"),

    DELETE(1,"删除")
    ;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private Integer code;

    private String msg;

    DeleteEnum(Integer code, String msg){

        this.code = code;
        this.msg = msg;
    }
}