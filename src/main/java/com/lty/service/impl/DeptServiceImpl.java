package com.lty.service.impl;

import com.lty.dao.DeptDao;
import com.lty.enity.Dept;
import com.lty.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * date: 2023/5/21 13:37
 *
 * @author Liu tai yuan
 */
@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptDao deptDao;

    @Override
    public Dept getDeptById(Integer deptId) {
        return deptDao.getDeptUsers(deptId);
    }
}
