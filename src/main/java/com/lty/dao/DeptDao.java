package com.lty.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lty.enity.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * date: 2023/5/20 21:31
 *
 * @author Liu tai yuan
 */
@Mapper
@Repository
public interface DeptDao {
    String getDeptName(@Param("deptId") Integer id);

    Dept getDeptUsers(@Param("deptId") Integer id);
}
