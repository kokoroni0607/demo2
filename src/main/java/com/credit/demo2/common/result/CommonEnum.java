package com.credit.demo2.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author hj
 * 2019-05-27 8:45
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum CommonEnum implements ResultCode {

    SUCCESS(200, "成功", true),
    FAIL(500, "失败", false),
    ;

    private int code;
    private String tip;
    private boolean success;

    @Override
    public int code() {
        return code;
    }

    @Override
    public String tip() {
        return tip;
    }
    @Override
    public boolean success() {
        return success;
    }
}
