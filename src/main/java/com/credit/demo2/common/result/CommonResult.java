package com.credit.demo2.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


/**
 * @author hj
 * 2019-05-24 17:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResult {

    /**
     * 分页数据相关
     */
    private Object data;

    @JsonInclude(NON_NULL) //为null时不序列化该字段
    private Long total;

    /**
     * 通用枚举信息
     */
    private int code;
    private boolean isSuccess;
    private String message;


    /**
     * 带分页参数的返回
     *
     * @param resultCode 通用枚举
     * @param data       数据
     * @param total      分页总条数
     * @return CommonResult
     */
    public static CommonResult success(ResultCode resultCode, Object data, Long total) {
        return CommonResult.builder()
                .data(data)
                .total(total)
                .code(resultCode.code())
                .message(resultCode.tip())
                .isSuccess(resultCode.success())
                .build();
    }


    public static CommonResult success(Object data) {
        return CommonResult.builder()
                .data(data)
                .code(CommonEnum.SUCCESS.code())
                .message(CommonEnum.SUCCESS.tip())
                .isSuccess(CommonEnum.SUCCESS.success())
                .build();
    }


    public static CommonResult success(Object data, Long total) {
        return CommonResult.builder()
                .data(data)
                .total(total)
                .code(CommonEnum.SUCCESS.code())
                .message(CommonEnum.SUCCESS.tip())
                .isSuccess(CommonEnum.SUCCESS.success())
                .build();
    }

    public static CommonResult success() {
        return CommonResult.builder()
                .code(CommonEnum.SUCCESS.code())
                .message(CommonEnum.SUCCESS.tip())
                .isSuccess(CommonEnum.SUCCESS.success())
                .build();
    }


    public static CommonResult fail() {
        return CommonResult.builder()
                .code(CommonEnum.FAIL.code())
                .message(CommonEnum.FAIL.tip())
                .isSuccess(CommonEnum.FAIL.success())
                .build();
    }

    public static CommonResult fail(Object data) {
        return CommonResult.builder()
                .data(data)
                .code(CommonEnum.FAIL.code())
                .message(CommonEnum.FAIL.tip())
                .isSuccess(CommonEnum.FAIL.success())
                .build();
    }

    /**
     * 自定义枚举信息的失败方法
     *
     * @param resultCode 公共枚举
     * @return CommonResult
     */
    public static CommonResult fail(ResultCode resultCode) {
        return CommonResult.builder()
                .code(resultCode.code())
                .message(resultCode.tip())
                .isSuccess(resultCode.success())
                .build();
    }

    /**
     * 自定义枚举信息的失败方法
     *
     * @param resultCode 公共枚举
     * @return CommonResult
     */
    public static CommonResult fail(ResultCode resultCode, Object data) {
        return CommonResult.builder()
                .data(data)
                .code(resultCode.code())
                .message(resultCode.tip())
                .isSuccess(resultCode.success())
                .build();
    }

    /**
     * 自定义状态码的失败方法
     *
     * @return CommonResult
     */
    public static CommonResult fail(int code, String tip, boolean success, Object data) {
        return CommonResult.builder()
                .data(data)
                .code(code)
                .message(tip)
                .isSuccess(success)
                .build();
    }


}
