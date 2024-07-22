package com.zgj.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.reggie.entity.Dish;
import dto.DishDto;

public interface IDishService extends IService<Dish> {
        //新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish dish_flavor
     void saveWithFlavor(DishDto dishDto);

    DishDto queryWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
}
