package com.zgj.reggie.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.bcel.internal.generic.MULTIANEWARRAY;
import com.zgj.reggie.common.R;
import com.zgj.reggie.entity.*;
import com.zgj.reggie.service.*;
import dto.DishDto;
import dto.OrdersDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {

    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private IAddressBookService addressBookService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IShoppingCartService shoppingCartService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number){
        //构造分页构造器
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        //构建条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(number),Orders::getNumber,number);
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);
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
            AddressBook addressBook = addressBookService.getById(addressBookId);//根据id查询对象
            if (addressBook != null) {
                String detail = addressBook.getDetail();
                ordersDto.setAddress(detail);
            }
            return ordersDto;
        }).collect(Collectors.toList());
        ordersDtoPage.setRecords(ordersDtoList);
        return R.success(ordersDtoPage);
    }

    @GetMapping("/userPage")
    public R<Page> userPage(HttpServletRequest request, int page, int pageSize){
        Long userId = (Long)request.getSession().getAttribute("user");
        //构造分页构造器
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        //构建条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!StringUtils.isEmpty(userId),Orders::getUserId,userId);
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);
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
            AddressBook addressBook = addressBookService.getById(addressBookId);//根据id查询对象
            if (addressBook != null) {
                String detail = addressBook.getDetail();
                ordersDto.setAddress(detail);
            }
            List<OrderDetail> orderDetailList = orderDetailService.lambdaQuery().eq(OrderDetail::getOrderId, ordersDto.getId()).list();
            ordersDto.setOrderDetails(orderDetailList);
            return ordersDto;
        }).collect(Collectors.toList());
        ordersDtoPage.setRecords(ordersDtoList);
        return R.success(ordersDtoPage);
    }

    @PostMapping("/submit")
    @Transactional
    public R<String> submit(@RequestBody Orders orders,HttpServletRequest request){
        Long userId = (Long) request.getSession().getAttribute("user");
        User user = userService.lambdaQuery().eq(User::getId, userId).one();
        orders.setUserId(userId);
        orders.setUserName(user.getName());
        orders.setPhone(user.getPhone());
        orders.setStatus(2);
        AddressBook addressBook = addressBookService.lambdaQuery().eq(AddressBook::getId,orders.getAddressBookId()).one();
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        List<BigDecimal> bigDecimalList = shoppingCartService.lambdaQuery().eq(ShoppingCart::getUserId, userId).list().stream().map(item -> {
            BigDecimal bigDecimal = item.getAmount().multiply(BigDecimal.valueOf(item.getNumber()));
            return bigDecimal;
        }).collect(Collectors.toList());
        BigDecimal add = BigDecimal.valueOf(0);
        BigDecimal bigDecimal1 = BigDecimal.valueOf(0);
        for (BigDecimal bigDecimal : bigDecimalList) {
            add = bigDecimal1.add(bigDecimal).add(add);
        }
        orders.setAmount(add);
        String uuid = UUID.fastUUID().toString();
        orders.setNumber(uuid);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        ordersService.save(orders);

        Long orderId = ordersService.lambdaQuery().eq(Orders::getNumber, uuid).one().getId();
//        orderDetail.setOrderId(orderId);
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(lambdaQueryWrapper);
        List<OrderDetail> orderDetailList = shoppingCartList
                .stream().map(item -> {
                    OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(item, orderDetail,"id");
            orderDetail.setOrderId(orderId);
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailService.saveBatch(orderDetailList);
        shoppingCartService.remove(lambdaQueryWrapper);
        return R.success("成功！");
    }

    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders){
        log.info(orders.toString());
        ordersService.updateById(orders);
        return R.success("成功！");
    }
}
