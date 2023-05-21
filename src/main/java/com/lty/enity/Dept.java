package com.lty.enity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * date: 2023/5/20 21:32
 *
 * @author Liu tai yuan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dept {
    private Integer id;
    private String deptName;
    private Integer companyNumber;
    private List<User> users;
}
