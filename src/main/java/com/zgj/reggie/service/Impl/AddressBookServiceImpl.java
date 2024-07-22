package com.zgj.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.reggie.entity.AddressBook;
import com.zgj.reggie.mapper.AddressBookMapper;
import com.zgj.reggie.service.IAddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {
}
