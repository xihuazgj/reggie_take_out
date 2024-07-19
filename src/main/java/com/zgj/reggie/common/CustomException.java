package com.zgj.reggie.common;

/*
* 自定义业务异常类
* */

import org.springframework.stereotype.Component;


public class CustomException extends RuntimeException{

    public CustomException(String message){

        super(message);
    }


}
