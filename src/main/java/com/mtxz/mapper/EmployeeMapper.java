package com.mtxz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mtxz.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/14
 * Time: 22:37
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
