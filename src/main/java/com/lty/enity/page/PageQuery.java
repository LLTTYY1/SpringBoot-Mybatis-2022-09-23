package com.lty.enity.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * date: 2023/5/20 20:01
 *
 * @author Liu tai yuan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQuery {
    //起始的页码 默认为第一页
    private Integer pageNum = 1;
    //每页显示的个数为3个信息
    private Integer pageSize = 3;
}
