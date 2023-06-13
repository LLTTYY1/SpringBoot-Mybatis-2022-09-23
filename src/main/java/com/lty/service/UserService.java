package com.lty.service;

import com.github.pagehelper.PageInfo;
import com.lty.enity.User;
import com.lty.enity.page.PageQuery;

import java.util.List;

/**
 * date: 2023/5/20 15:46
 *
 * @author Liu tai yuan
 */
public interface UserService {
    List<User> getAllUser();

    PageInfo<User> getAllUserByPage(PageQuery pageQuery);

    PageInfo<User> getAllUserByPageAndName(PageQuery pageQuery,String name);

    int updateUser(int id,String address);

    int deleteById(int id);

    int insertUser(User user);
//    模糊查询
    PageInfo<User> getAllUserByUserName(String name);
}
