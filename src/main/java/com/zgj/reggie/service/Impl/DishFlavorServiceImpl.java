package com.zgj.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.reggie.entity.DishFlavor;
import com.zgj.reggie.mapper.DishFlavorMapper;
import com.zgj.reggie.service.IDishFlavorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements IDishFlavorService {


    @Override
    public List<DishFlavor> queryWithDishId(Long id) {
        List<DishFlavor> list = lambdaQuery().eq(DishFlavor::getDishId, id).list();
        return list;
    }
}
