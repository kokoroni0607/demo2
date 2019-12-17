package com.credit.demo2.controller;


import com.credit.demo2.common.result.CommonResult;
import com.credit.demo2.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhu.weiming
 * @since 2019-12-17
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/test")
    public CommonResult test(){
        return CommonResult.success("test succeed");
    }
}
