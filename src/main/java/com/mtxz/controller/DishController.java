package com.mtxz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mtxz.common.R;
import com.mtxz.dto.DishDto;
import com.mtxz.entity.Category;
import com.mtxz.entity.Dish;
import com.mtxz.entity.DishFlavor;
import com.mtxz.service.CategoryService;
import com.mtxz.service.DishFlavorService;
import com.mtxz.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/16
 * Time: 18:26
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param dishDto 接收参数包含Dish和DishFlavor两个实体，所以用DishDto来封装这两个实体
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("dishDto：{}", dishDto.toString());
        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    /**
     * 菜品分页查询
     * 先查到Page<Dish>信息，因为Page<Dish>的records数据里有categoryId属性但是页面要求的是categoryName属性
     * 所以要用Page<DishDto>  添加categoryName属性
     * 先把Page<Dish>里的total、size等除了records属性的数据拷贝至Page<DishDto>
     * 然后依次遍历Page<Dish>中records里的categoryId来查找页面所需的分类名categoryName
     * 将这个分类名categoryName放到Page<DishDto>里
     * 不懂建议debug查看pageInfo和dishDtoPage的值
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //创建分页构造器对象
        Page<Dish> pageInfo = new Page(page, pageSize);
        //Dish里不包含菜品分类名，利用DishDTo实现
        Page<DishDto> dishDtoPage = new Page(page, pageSize);
        //构造条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getStatus);
        queryWrapper.orderByAsc(Dish::getCategoryId);
        queryWrapper.orderByAsc(Dish::getPrice);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行查询
        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        //通过records创建包含分类名的records
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            //获取id对应的分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                //获取分类对应名称
                String categoryName = category.getName();
                //set进去
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        //把刚才忽略拷贝的records补上
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查菜品信息及对应的口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("dishDto：{}", dishDto.toString());
        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功");
    }

    /**
     * 起售停售---批量起售停售
     *
     * @param status 0-停售 1-起售
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable int status, @RequestParam Long[] ids) {
        log.info("status:{},ids:{}", status, ids);
        dishService.updateStatus(status, ids);
        return R.success("状态改变成功");
    }

    /**
     * 删除菜品
     *
     * @param ids 这里应该换成传Dish，通用性强
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam Long[] ids) {
        log.info("ids:{}", ids);
        dishService.deleteByIds(ids);
        return R.success("删除成功");
    }

    /**
     * 根据条件查询对应菜品数据
     *
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish) {
//        //构造条件
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        //设置起售状态
//        queryWrapper.eq(Dish::getStatus,1);
//        queryWrapper.orderByAsc(Dish::getSort);
//        queryWrapper.orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        //构造条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //设置起售状态
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            //获取id对应的分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                //获取分类对应名称
                String categoryName = category.getName();
                //set进去
                dishDto.setCategoryName(categoryName);
            }

            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);

            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());


        return R.success(dishDtoList);
    }
}
