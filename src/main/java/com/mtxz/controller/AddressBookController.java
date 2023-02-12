package com.mtxz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mtxz.common.BaseContext;
import com.mtxz.common.R;
import com.mtxz.entity.AddressBook;
import com.mtxz.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址管理
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/18
 * Time: 10:00
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        //先将该用户的所有默认地址都设为0
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(updateWrapper);

        //将传过来的地址设为1
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);

        return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     *
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 查询用户的所有地址
     *
     * @param addressBook
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);

        //构造条件
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        //执行查询
        List<AddressBook> list = addressBookService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<AddressBook> update(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}",addressBook);
        addressBookService.updateById(addressBook);

        return R.success(addressBook);
    }

    /**
     * 删除地址
     * @param addressBook
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}",addressBook);
        addressBookService.removeById(addressBook.getId());

        return R.success("删除成功");
    }
}
