package com.zgj.reggie.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.reggie.common.R;
import com.zgj.reggie.entity.Category;
import com.zgj.reggie.entity.Dish;
import com.zgj.reggie.entity.Employee;
import com.zgj.reggie.service.ICategoryService;
import com.zgj.reggie.service.IDishFlavorService;
import com.zgj.reggie.service.IDishService;
import dto.DishDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private IDishService dishService;

    @Autowired
    private IDishFlavorService dishFlavorService;

    @Autowired
    private ICategoryService categoryService;
    /*
    *菜品信息分页查询
    * */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //构建条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name),Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getUpdateTime);
        //执行查询
        dishService.page(pageInfo,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtoList = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            //对象拷贝
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId(); //分类id
            Category category = categoryService.getById(categoryId);//根据id查询分类对象
            if (category != null) {
                String categoryName = category.getName(); //拿出category中的categoryname
                dishDto.setCategoryName(categoryName);//将categoryname赋值给dishdto
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    /*
    * 新增菜品
    * */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功！");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        dishService.removeByIds(ids);
        return R.success("批量删除成功！");
    }
    @GetMapping("/{id}")
    public R<DishDto> query(@PathVariable Long id){
        Dish dish = dishService.getById(id);
        DishDto dishDto = dishService.queryWithFlavor(id);
        BeanUtils.copyProperties(dish,dishDto);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功！");
    }
}

