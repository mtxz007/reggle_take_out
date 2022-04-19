package com.mtxz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mtxz.common.CustomException;
import com.mtxz.entity.Category;
import com.mtxz.entity.Dish;
import com.mtxz.entity.Setmeal;
import com.mtxz.mapper.CategoryMapper;
import com.mtxz.service.CategoryService;
import com.mtxz.service.DishService;
import com.mtxz.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/16
 * Time: 8:49
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        //查询当前分类是否已经关联了菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int dishCount = dishService.count(dishLambdaQueryWrapper);
        //如果当前分类关联了菜品
        if (dishCount > 0) {
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        //查询当前分类是否已经关联了套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        //如果当前分类关联了套餐
        if (setmealCount > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //正常删除
        super.removeById(id);
    }
}
