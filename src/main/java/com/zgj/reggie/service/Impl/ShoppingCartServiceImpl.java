package com.zgj.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.reggie.entity.ShoppingCart;
import com.zgj.reggie.mapper.ShoppingCartMapper;
import com.zgj.reggie.service.IShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {
}
