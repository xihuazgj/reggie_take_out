package com.zgj.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.reggie.entity.Setmeal;
import dto.SetmealDto;

public interface ISetmealService extends IService<Setmeal> {
    SetmealDto queryWithSetmeal(Long id);

    void saveWithDish(SetmealDto setmealDto);

    void updateWithDish(SetmealDto setmealDto);
}
