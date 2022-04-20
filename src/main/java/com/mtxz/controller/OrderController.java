package com.mtxz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mtxz.common.BaseContext;
import com.mtxz.common.R;
import com.mtxz.dto.OrdersDto;
import com.mtxz.entity.AddressBook;
import com.mtxz.entity.OrderDetail;
import com.mtxz.entity.Orders;
import com.mtxz.entity.User;
import com.mtxz.service.AddressBookService;
import com.mtxz.service.OrderDetailService;
import com.mtxz.service.OrderService;
import com.mtxz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/18
 * Time: 21:46
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);
        orderService.submit(orders);

        return R.success("下单成功");
    }

    /**
     * 用户分页查询订单
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize) {
        //创建分页构造器
        Page<Orders> pageInfo = new Page(page, pageSize);
        Page<OrdersDto> dtoPage = new Page(page, pageSize);

        //构造条件
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        orderService.page(pageInfo, queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Orders> ordersList = pageInfo.getRecords();

        List<OrdersDto> list = ordersList.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);

            //查orderDetails并set
            Long orderId = item.getId();
            LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(OrderDetail::getOrderId, orderId);
            List<OrderDetail> orderDetailList = orderDetailService.list(lambdaQueryWrapper);
            ordersDto.setOrderDetails(orderDetailList);

            //查ordersDto剩余属性，不知道用不用得到，先查了
            //查user表的用户名
            User user = userService.getById(userId);
            ordersDto.setUserName(user.getName());
            //查地址表   好像是按默认的查
            LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AddressBook::getUserId, userId);
            wrapper.eq(AddressBook::getIsDefault, 1);
            AddressBook addressBook = addressBookService.getOne(wrapper);
            ordersDto.setPhone(addressBook.getPhone());
            ordersDto.setAddress(addressBook.getDetail());
            ordersDto.setConsignee(addressBook.getConsignee());

            return ordersDto;
        }).collect(Collectors.toList());

        //把刚才忽略拷贝的records补上
        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    /**
     * 员工分页查询订单
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> pages(int page, int pageSize, String number, String beginTime, String endTime) {
        log.info("page:{},pageSize:{},number={},beginTime={},endTime={}", page, pageSize, number, beginTime, endTime);
        //创建分页构造器
        Page<Orders> pageInfo = new Page(page, pageSize);

        //构造条件
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(number), Orders::getNumber, number);
        queryWrapper.between(StringUtils.isNotEmpty(beginTime)&&StringUtils.isNotEmpty(endTime),
                Orders::getOrderTime,beginTime,endTime);
        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 更改订单状态
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders) {
        log.info("orders:{}",orders.toString());
        //设置条件更新状态
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Orders::getId,orders.getId());
        updateWrapper.set(orders.getStatus()!=null,Orders::getStatus,orders.getStatus());

        orderService.update(updateWrapper);

        return R.success("状态更新成功");
    }
}
