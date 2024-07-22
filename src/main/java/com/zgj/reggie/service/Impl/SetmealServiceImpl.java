package com.zgj.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.reggie.entity.DishFlavor;
import com.zgj.reggie.entity.Setmeal;
import com.zgj.reggie.entity.SetmealDish;
import com.zgj.reggie.mapper.SetmealMapper;
import com.zgj.reggie.service.ISetmealDishService;
import com.zgj.reggie.service.ISetmealService;
import dto.DishDto;
import dto.SetmealDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements ISetmealService {

    @Autowired
    private ISetmealDishService setmealDishService;
    @Override
    public SetmealDto queryWithSetmeal(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        List<SetmealDish> setmealDishes = setmealDishService.queryWithSetmealId(id);
        setmealDto.setSetmealDishes(setmealDishes);
        return setmealDto;
    }

    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        Long setmealId = setmealDto.getId();//获得菜品id
        //菜品口味
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表dish_flavor
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //更新setmeal表信息
        this.updateById(setmealDto);
        //清理当前套餐对应菜品数据 --setmeal_dish表的delete的操作
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper =
                new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(lambdaQueryWrapper);
        //添加当前提交过来的菜品数据--setmeal_dish表的insert的操作
        Long setmealDtoId = setmealDto.getId();//获得套餐id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDtoId);
            return item;
        }).collect(Collectors.toList());
        //保存套餐菜品数据到套餐菜品表setmeal_dish
        setmealDishService.saveBatch(setmealDishes);
    }
}
