package com.lty.controller;
import com.github.pagehelper.PageInfo;
import com.lty.enity.User;
import com.lty.enity.page.PageQuery;
import com.lty.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * date: 2023/5/19 21:11
 *
 * @author Liu tai yuan
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController{
    @Autowired
    private UserService userService;

    @RequestMapping("/getAllUser")
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

    @RequestMapping("/getAllUserByPage")
    public List<User> getAllUserByPage(PageQuery pageQuery){
        PageInfo<User> allUserByPage = userService.getAllUserByPage(pageQuery);
        List<User> list = allUserByPage.getList();
        return list;
    }

    @RequestMapping("/getAllUserByPageAndName")
    public List<User> getAllUserByPageAndName(PageQuery pageQuery,String name){
        PageInfo<User> allUserByPage = userService.getAllUserByPageAndName(pageQuery,name);
        List<User> list = allUserByPage.getList();
        return list;
    }
    @RequestMapping("/update")
    public int updateUser(int id,String address){
        return userService.updateUser(id,address);
    }

    @RequestMapping("/delete")
    public int updateUser(int id){
        return userService.deleteById(id);
    }

    @RequestMapping("/insert")
    public int updateUser(){
        return userService.insertUser(new User(null,"新增","开发部","河南",1));
    }

    /**
     * 防止用户篡改分页参数，不再提供分页对象PageQuery
     * @param name
     * @return
     */
    @RequestMapping("/getAllUserByName")
    public List<User> getAllUserByName(String name){
        PageInfo<User> allUserByUserName = userService.getAllUserByUserName(name);
        int pageNum = allUserByUserName.getPageNum();
        log.info("当前页的个数:{}",pageNum);
        int pages = allUserByUserName.getPages();
        log.info("当前页的页码为:{}",pages);
        return allUserByUserName.getList();
    }
}
