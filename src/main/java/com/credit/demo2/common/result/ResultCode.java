package com.credit.demo2.common.result;

/**
 * @author hj
 * 2019-05-27 10:53
 * 公共提示接口
 */
public interface ResultCode {
    int code();

    String tip();

    boolean success();
}
