package com.zgj.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.reggie.entity.SetmealDish;
import com.zgj.reggie.mapper.SetmealDishMapper;
import com.zgj.reggie.service.ISetmealDishService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements ISetmealDishService {
    @Override
    public List<SetmealDish> queryWithSetmealId(Long id) {
        return lambdaQuery().eq(SetmealDish::getSetmealId, id).list();
    }
}
