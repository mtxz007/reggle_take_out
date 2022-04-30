package com.mtxz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mtxz.common.R;
import com.mtxz.dto.SetmealDto;
import com.mtxz.entity.Category;
import com.mtxz.entity.Setmeal;
import com.mtxz.service.CategoryService;
import com.mtxz.service.SetmealDishService;
import com.mtxz.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 套餐管理
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/17
 * Time: 10:50
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    @ApiOperation("新增套餐接口")
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息:{}", setmealDto);
        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    @ApiOperation("删除套餐接口")
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);
        setmealService.removeWithDish(ids);

        return R.success("删除成功");
    }

    /**
     * 套餐分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("套餐分页查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true),
            @ApiImplicitParam(name = "name", value = "套餐名称", required = false),
    })
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page(page, pageSize);
        Page<SetmealDto> dtoPage = new Page(page, pageSize);

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            //获取分类名称
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    /**
     * 根据id查对应套餐信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查对应套餐接口")
    public R<SetmealDto> getById(@PathVariable Long id) {
        log.info("id为：{}", id);
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);

        return R.success(setmealDto);
    }

    /**
     * 修改套餐
     *
     * @param setmealDto
     * @return
     */
    @PutMapping
    @ApiOperation("修改套餐接口")
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        log.info("setmealDto:{}", setmealDto.toString());
        setmealService.updateWithDish(setmealDto);

        return R.success("修改成功");
    }

    /**
     * 起售停售---批量起售停售
     *
     * @param status 0-停售 1-起售
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("批量起售停售套餐接口")
    public R<String> updateStatus(@PathVariable int status, @RequestParam List<Long> ids) {
        log.info("status:{},ids:{}", status, ids);
        setmealService.updateStatus(status, ids);

        return R.success("状态改变成功");
    }

    /**
     * 根据条件查套餐数据
     *
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId + '_' + #setmeal.status")
    @ApiOperation("根据条件查套餐接口")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }
}
