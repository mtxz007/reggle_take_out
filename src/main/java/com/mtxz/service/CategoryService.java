package com.mtxz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mtxz.entity.Category;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/16
 * Time: 8:49
 */
public interface CategoryService extends IService<Category> {
    /**
     * 根据id删除分类，在删除之前判断
     *
     * @param id
     */
    public void remove(Long id);
}
