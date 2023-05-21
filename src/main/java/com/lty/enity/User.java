package com.lty.enity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * date: 2023/5/20 13:15
 *
 * @author Liu tai yuan
 */
@Data // lombok提供的注解 自动生成getter,setter方法
@AllArgsConstructor //所有的有参构造器
@NoArgsConstructor  //所有的无参构造器
public class User {
    private Integer id;
    private String name;
    private String deptName;
    private String address;
    private Integer deptId;
}
