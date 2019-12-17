package com.credit.demo2.exception;

import com.credit.demo2.common.result.CommonResult;
import com.credit.demo2.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author weiming.zhu
 * @date 2019/12/17 15:17
 */
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(DemoException.class)
    public CommonResult handleException(DemoException e) {
        log.error("全局异常处理收到异常, msg : [{}],异常堆栈为：[{}]", e.getMessage(), ExceptionUtil.getStackTrace(e));
        return CommonResult.fail(e.getMessage());
    }
}
