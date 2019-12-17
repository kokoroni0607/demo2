package com.credit.demo2.controller;

import com.credit.demo2.common.result.CommonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weiming.zhu
 * @date 2019/12/17 15:34
 */
@RestController
@RequestMapping("/index")
public class WelcomeController {
    @RequestMapping("/welcome")
    public CommonResult welcome(String username) {
        return CommonResult.success("login success" + username + "! welcome!");
    }
}
