package com.mtxz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mtxz.common.R;
import com.mtxz.dto.SetmealDto;
import com.mtxz.entity.Setmeal;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/16
 * Time: 9:59
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐及其菜品关联关系
     *
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐及其菜品关联关系
     *
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    /**
     * 根据id查询套餐及其菜品关系
     *
     * @param id
     */
    public SetmealDto getByIdWithDish(Long id);

    /**
     * 修改套餐
     *
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);

    /**
     * 根据id批量起售停售
     *
     * @param status
     * @param ids
     */
    public void updateStatus(int status, List<Long> ids);
}
