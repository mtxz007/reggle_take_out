package com.mtxz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mtxz.entity.Orders;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/18
 * Time: 21:44
 */
public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
