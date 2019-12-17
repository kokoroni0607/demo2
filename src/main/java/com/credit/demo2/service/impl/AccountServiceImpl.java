package com.credit.demo2.service.impl;

import com.credit.demo2.entity.Account;
import com.credit.demo2.mapper.AccountMapper;
import com.credit.demo2.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhu.weiming
 * @since 2019-12-17
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

}
