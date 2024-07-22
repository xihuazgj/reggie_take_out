package com.zgj.reggie.controller;


import com.zgj.reggie.common.R;
import com.zgj.reggie.entity.ShoppingCart;
import com.zgj.reggie.service.IShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;
    @GetMapping("/list")
    public R<List<ShoppingCart>> shoppingCartlist(){
        List<ShoppingCart> shoppingCartList = shoppingCartService.list();

        return R.success(shoppingCartList);
    }
}
