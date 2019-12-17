package com.credit.demo2.mapper;

import com.credit.demo2.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhu.weiming
 * @since 2019-12-17
 */
@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRoleByRealmId(Integer id);

    List<Role> getRoleByAccount(Integer id);
}
