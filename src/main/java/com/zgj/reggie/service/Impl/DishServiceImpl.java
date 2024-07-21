package com.zgj.reggie.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.reggie.entity.Dish;
import com.zgj.reggie.entity.DishFlavor;
import com.zgj.reggie.mapper.DishMapper;
import com.zgj.reggie.service.IDishFlavorService;
import com.zgj.reggie.service.IDishService;
import dto.DishDto;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

    @Autowired
    private IDishFlavorService dishFlavorService;
    /*
    * 新增菜品，同时保存对应的口味数据
    * */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        Long dishId = dishDto.getId();//获得菜品id
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }
    @Override
    public DishDto queryWithFlavor(Long id) {
        DishDto dishDto = new DishDto();
        List<DishFlavor> dishFlavorList = dishFlavorService.queryWithDishId(id);
        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        String flavors = dishDto.getFlavors().toString();
        dishFlavor.setValue(flavors);
        dishFlavorService.updateById(dishFlavor);
    }
    }

