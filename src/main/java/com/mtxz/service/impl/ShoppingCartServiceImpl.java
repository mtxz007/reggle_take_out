package com.mtxz.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mtxz.entity.ShoppingCart;
import com.mtxz.mapper.ShoppingCartMapper;
import com.mtxz.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/18
 * Time: 18:48
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
