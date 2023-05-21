package com.lty.controller;

import com.lty.enity.Dept;
import com.lty.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * date: 2023/5/21 13:39
 *
 * @author Liu tai yuan
 */
@RestController
@RequestMapping("/dept")
public class DeptController {
    @Autowired
    private DeptService deptService;

    @RequestMapping("/getUsers")
    public Dept getDeptUsers(Integer deptId){
        return deptService.getDeptById(deptId);
    }
}
