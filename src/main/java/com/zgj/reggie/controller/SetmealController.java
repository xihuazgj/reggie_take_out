package com.zgj.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.reggie.common.R;
import com.zgj.reggie.entity.Category;
import com.zgj.reggie.entity.Dish;
import com.zgj.reggie.entity.Setmeal;
import com.zgj.reggie.entity.SetmealDish;
import com.zgj.reggie.service.ICategoryService;
import com.zgj.reggie.service.ISetmealDishService;
import com.zgj.reggie.service.ISetmealService;
import dto.DishDto;
import dto.SetmealDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private ISetmealService setmealService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ISetmealDishService setmealDishService;
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //构造分页构造器
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //构建条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name),Setmeal::getName,name);
        //添加排序条件
        queryWrapper.orderByAsc(Setmeal::getUpdateTime);
        //执行查询
        setmealService.page(pageInfo,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> setmealDtoList = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId(); //分类id
            Category category = categoryService.getById(categoryId);//根据id查询分类对象
            if (category != null) {
                String categoryName = category.getName(); //拿出category中的categoryname
                setmealDto.setCategoryName(categoryName);//将categoryname赋值给dishdto
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(setmealDtoList);
        return R.success(setmealDtoPage);
    }

    @GetMapping("/{id}")
    public R<SetmealDto> query(@PathVariable Long id){
        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = setmealService.queryWithSetmeal(id);
        BeanUtils.copyProperties(setmeal,setmealDto);
        return R.success(setmealDto);
    }


    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功！");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeByIds(ids);
        return R.success("批量删除成功！");
    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.success("修改菜品成功！");
    }

    @PostMapping("/status/{code}")
    public R<String> updateStatus(@PathVariable Integer code,@RequestParam List<Long> ids){
        //通过ids查询菜品
        List<Setmeal> setmealList = setmealService.listByIds(ids);
        //更新菜品的状态码
        setmealList.forEach(item -> {
            item.setStatus(code);
            setmealService.updateById(item);
        });
        return R.success("成功！");
    }
}
