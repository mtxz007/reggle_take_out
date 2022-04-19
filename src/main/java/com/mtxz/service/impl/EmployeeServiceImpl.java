package com.mtxz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mtxz.entity.Employee;
import com.mtxz.mapper.EmployeeMapper;
import com.mtxz.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
  Created by IntelliJ IDEA.
  author: ylb
  Date: 2022/4/14
  Time: 22:39
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {

}
