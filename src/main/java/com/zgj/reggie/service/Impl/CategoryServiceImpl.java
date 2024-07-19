package com.zgj.reggie.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.reggie.common.CustomException;
import com.zgj.reggie.entity.Category;
import com.zgj.reggie.entity.Dish;
import com.zgj.reggie.entity.Setmeal;
import com.zgj.reggie.mapper.CategoryMapper;
import com.zgj.reggie.service.ICategoryService;
import com.zgj.reggie.service.IDishService;
import com.zgj.reggie.service.ISetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends
        ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Autowired
    private IDishService dishService;

    @Autowired
    private ISetmealService setmealService;
    /*
    * 根据id删除分类，删除之前要进行判断
    * */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper =
                new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0){
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除！");
        }
        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper =
                new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0){
            //已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除！");
        }
        //正常删除分类
        super.removeById(id);
    }
}
