package com.lty.dao;

import com.lty.enity.User;
import com.lty.enity.page.PageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * date: 2023/5/20 13:24
 *
 * @author Liu tai yuan
 */
@Mapper
@Repository
public interface UserDao {
       //获取所有的用户信息
       List<User> getAllUser();
       //获取所有的用户信息并且分页
       List<User> getAllUserByPage(PageQuery pageQuery);
       //根据用户用户名进行模糊查询
       List<User> getAllUserByPageAndName(PageQuery pageQuery,@Param("name") String name);

       List<User> getUsers(@Param("deptId") Integer id);

       int UpdateUserById(@Param("id") Integer id, @Param("address") String address);

       int deleteById(@Param("id") Integer id);

       int insertUser(@Param("user") User user);
//       模糊查询
       List<User> getAllUserByUserName(@Param("name") String username);
}
