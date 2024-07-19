package com.zgj.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.reggie.entity.Category;

public interface ICategoryService extends IService<Category>{
    void remove(Long id);
}
