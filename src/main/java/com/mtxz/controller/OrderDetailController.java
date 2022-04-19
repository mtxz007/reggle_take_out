package com.mtxz.controller;

import com.mtxz.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/18
 * Time: 21:48
 */
@RestController
@RequestMapping("/orderDetail")
@Slf4j
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;


}
