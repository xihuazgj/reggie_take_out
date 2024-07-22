package com.zgj.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.reggie.entity.Orders;
import com.zgj.reggie.mapper.OrdersMapper;
import com.zgj.reggie.service.IOrdersService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {
}
