package com.zgj.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.reggie.common.R;
import com.zgj.reggie.entity.AddressBook;
import com.zgj.reggie.entity.Category;
import com.zgj.reggie.entity.Dish;
import com.zgj.reggie.entity.Orders;
import com.zgj.reggie.service.IAddressBookService;
import com.zgj.reggie.service.IOrdersService;
import dto.DishDto;
import dto.OrdersDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {

    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private IAddressBookService addressBookService;
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number){
        //构造分页构造器
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        //构建条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(number),Orders::getNumber,number);
        //添加排序条件
        queryWrapper.orderByAsc(Orders::getOrderTime);
        //执行查询
        ordersService.page(pageInfo,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtoList = records.stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            //对象拷贝
            BeanUtils.copyProperties(item, ordersDto);
            Long addressBookId = item.getAddressBookId();//地址id
            AddressBook addressBook = addressBookService.getById(addressBookId);//根据id查询分类对象
            if (addressBook != null) {
                String detail = addressBook.getDetail();//拿出category中的categoryname
                ordersDto.setAddress(detail);//将categoryname赋值给dishdto
            }
            return ordersDto;
        }).collect(Collectors.toList());
        ordersDtoPage.setRecords(ordersDtoList);
        return R.success(ordersDtoPage);
    }
}
