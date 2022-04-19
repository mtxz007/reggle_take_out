package com.mtxz.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mtxz.entity.AddressBook;
import com.mtxz.mapper.AddressBookMapper;
import com.mtxz.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/18
 * Time: 9:59
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
