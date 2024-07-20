package com.zgj.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.reggie.entity.DishFlavor;

import java.util.List;

public interface IDishFlavorService extends IService<DishFlavor> {

    List<DishFlavor> queryWithDishId(Long id);
}
