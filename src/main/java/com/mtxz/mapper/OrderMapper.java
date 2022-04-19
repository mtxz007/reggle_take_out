package com.mtxz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mtxz.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/18
 * Time: 21:44
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
