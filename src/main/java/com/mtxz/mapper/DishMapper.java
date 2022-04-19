package com.mtxz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mtxz.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/16
 * Time: 9:56
 */
@Mapper
@Repository
public interface DishMapper extends BaseMapper<Dish> {
}
