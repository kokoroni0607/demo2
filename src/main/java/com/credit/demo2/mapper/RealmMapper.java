package com.credit.demo2.mapper;

import com.credit.demo2.entity.Realm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
public interface RealmMapper extends BaseMapper<Realm> {

}
