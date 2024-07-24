package com.zgj.reggie.controller;

import com.zgj.reggie.common.R;
import com.zgj.reggie.entity.AddressBook;
import com.zgj.reggie.service.IAddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/addressBook")
@RestController
@Slf4j
public class AddressBookController {

    @Autowired
    private IAddressBookService addressBookService;
    @GetMapping("/list")
    public R<List<AddressBook>> useraddress(HttpServletRequest request){
        Long userId = (Long) request.getSession().getAttribute("user");
        List<AddressBook> addressBookList = addressBookService.lambdaQuery().eq(AddressBook::getUserId, userId).list();
        return R.success(addressBookList);
    }

    @PostMapping

    public R<String> saveAddress(HttpServletRequest request,@RequestBody AddressBook addressBook){
        log.info(addressBook.toString());
        Long userId = (Long) request.getSession().getAttribute("user");
        if (addressBookService.lambdaQuery().eq(AddressBook::getUserId,userId).list() == null){
            addressBook.setUserId(userId);
            addressBook.setIsDefault(1);
            addressBookService.save(addressBook);
            return R.success("添加地址成功！");
        }
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return R.success("添加地址成功！");
    }
    @PutMapping("/default")
    public R<String> setDefault(@RequestBody AddressBook addressBook){
        List<AddressBook> addressBookList = addressBookService.list().stream().map((item) -> {
            item.setIsDefault(0);
            return item;
        }).collect(Collectors.toList());
        addressBookService.updateBatchById(addressBookList);
        AddressBook addressBook1 = addressBookService.lambdaQuery().eq(AddressBook::getId, addressBook.getId()).one();
        addressBook1.setIsDefault(1);
        addressBookService.updateById(addressBook1);
        return R.success("设置成功！");
    }
    @GetMapping("/{id}")
    public R<AddressBook> addressBook(@PathVariable("id") Long id){
        AddressBook addressBook = addressBookService.lambdaQuery().eq(AddressBook::getId, id).one();

        return R.success(addressBook);
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        addressBookService.removeById(ids);
        return R.success("删除成功");
    }

    @PutMapping
    public void update(@RequestBody AddressBook addressBook){
        addressBookService.removeById(addressBook.getId());
        addressBookService.save(addressBook);
    }

    @GetMapping("/default")
    public R<AddressBook> defaultAddress(HttpServletRequest request){
        Long userId = (Long) request.getSession().getAttribute("user");
        AddressBook addressBook = addressBookService.lambdaQuery().eq(AddressBook::getUserId, userId).eq(AddressBook::getIsDefault, 1).one();
        return R.success(addressBook);
    }
}
