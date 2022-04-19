package com.mtxz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mtxz.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/17
 * Time: 22:27
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
