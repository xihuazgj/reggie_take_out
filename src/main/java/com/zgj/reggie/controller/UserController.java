package com.zgj.reggie.controller;


import com.zgj.reggie.common.R;
import com.zgj.reggie.entity.User;
import com.zgj.reggie.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;
    @PostMapping("/login")

    public R<User> userLogin(HttpServletRequest request, @RequestBody User user){
        String userPhone = user.getPhone();
        User user1 = userService.lambdaQuery().eq(User::getPhone, userPhone).one();
        if (user1 != null){
            request.getSession().setAttribute("user",user1.getId());
            return R.success(user1);
        }

//        user.setName("张三");
//        user.setSex("男");
//        user.setIdNumber("511021522036987889");
//        user.setAvatar("ss");
        user.setStatus(1);
        userService.save(user);
        request.getSession().setAttribute("user",user.getId());
        return R.success(user);
    }

    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return R.success("退出成功！");
    }
}
