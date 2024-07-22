package com.zgj.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.reggie.entity.SetmealDish;

import java.util.List;

public interface ISetmealDishService extends IService<SetmealDish> {
    List<SetmealDish> queryWithSetmealId(Long id);
}
