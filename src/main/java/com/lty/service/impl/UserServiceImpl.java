package com.lty.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lty.dao.UserDao;
import com.lty.enity.User;
import com.lty.enity.page.PageQuery;
import com.lty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * date: 2023/5/20 15:47
 *
 * @author Liu tai yuan
 */
@Service
public class UserServiceImpl implements UserService {
    //注入我们的dao层,与数据库之间进行交互
    @Autowired
    private UserDao userDao;

    /**
     * 得到所有的用户信息
     * @return 返回的是一个List的User集合
     */
    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    /**
     * 通过分页插件pageHepler进行分页
     * @return 返回的是PageInfo
     */
    @Override
    public PageInfo<User> getAllUserByPage(PageQuery pageQuery) {
        //先开启分页
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        //返回的是一个PageInfo对象
        return new PageInfo<User>(userDao.getAllUserByPage(pageQuery));
    }

    /**
     * 通过模糊查询加上分页
     * @param pageQuery
     * @param name
     * @return
     */
    @Override
    public PageInfo<User> getAllUserByPageAndName(PageQuery pageQuery, String name) {
        //先开启分页
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        //返回的是一个PageInfo对象
        return new PageInfo<User>(userDao.getAllUserByPageAndName(pageQuery,name));
    }

    @Override
    public int updateUser(int id,String address) {
       return userDao.UpdateUserById(id,address);
    }

    @Override
    public int deleteById(int id) {
        return userDao.deleteById(id);
    }

    @Override
    public int insertUser(User user) {
        return userDao.insertUser(user);
    }

    @Override
    public PageInfo<User> getAllUserByUserName(String name) {
        PageQuery pageQuery = new PageQuery();
        //开启分页功能用PageHelper(PageHelper里面只给传递2个是参数，一个是起始页码，一个是每页的个数)
        PageHelper.startPage(pageQuery.getPageNum(),pageQuery.getPageSize());
        return new PageInfo<User>(userDao.getAllUserByUserName(name));
    }
}
