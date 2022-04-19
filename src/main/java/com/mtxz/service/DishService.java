package com.mtxz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mtxz.dto.DishDto;
import com.mtxz.entity.Dish;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/16
 * Time: 9:58
 */
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品，同时插入口味表
     *
     * @param dishDto
     */
    public void saveWithFlavor(DishDto dishDto);

    /**
     * 根据id查询菜品信息对应口味信息
     *
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id);

    /**
     * 更新菜品信息及其口味信息
     *
     * @param dishDto
     */
    public void updateWithFlavor(DishDto dishDto);

    /**
     * 根据id批量起售停售
     *
     * @param status
     * @param ids
     */
    public void updateStatus(int status, Long[] ids);

    /**
     * 批量删除
     *
     * @param ids
     */
    public void deleteByIds(Long[] ids);
}
