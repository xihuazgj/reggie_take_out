package com.zgj.reggie.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.zgj.reggie.common.R;
import com.zgj.reggie.entity.ShoppingCart;
import com.zgj.reggie.service.IShoppingCartService;
import dto.DishDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> shoppingCartlist() {
        List<ShoppingCart> shoppingCartList = shoppingCartService.lambdaQuery()
                .ge(ShoppingCart::getNumber, 0).orderByAsc(ShoppingCart::getCreateTime).list();
        return R.success(shoppingCartList);
    }

    @PostMapping("/add")
    public R<String> add(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");
        shoppingCart.setUserId(userId);
        ShoppingCart one = shoppingCartService.lambdaQuery().eq(ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getDishFlavor() != null,ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor())
                /*.eq(shoppingCart.getId() != null,ShoppingCart::getId,shoppingCart.getId())*/.one();
        if (one == null) {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
        }

        if (one != null) {
            Integer number = one.getNumber();
            String dishFlavor = one.getDishFlavor();
//            BigDecimal amount = one.getAmount();
            shoppingCartService.removeById(one.getId());
            shoppingCart.setNumber(number + 1);
//            shoppingCart.setAmount(amount.add(shoppingCart.getAmount()));
            shoppingCart.setDishFlavor(dishFlavor);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
        }

        return R.success("添加成功！");
    }

    @PostMapping("sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart) {
        log.info(shoppingCart.toString());
        ShoppingCart one = shoppingCartService.lambdaQuery().eq(shoppingCart.getId() !=null,ShoppingCart::getId, shoppingCart.getId()).one();
        Integer number = one.getNumber();
        if (number <= 1) {
            shoppingCartService.removeById(shoppingCart.getId());
        }
        if (number > 1) {
            one.setNumber(number - 1);
            shoppingCartService.updateById(one);
            return R.success("扣减成功！");
        }
        return R.success("扣减成功！");
    }

    @DeleteMapping("clean")
    public R<String> clean(HttpServletRequest request){
        Long userId = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(lambdaQueryWrapper);
        return R.success("清除成功！");
    }
}
