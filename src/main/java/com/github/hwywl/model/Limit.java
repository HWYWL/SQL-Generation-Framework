package com.github.hwywl.model;

import lombok.*;

/**
 * @author: YI
 * @description: 分页
 * @date: create in 2021/3/8 18:24
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Limit {
    /**
     * 开始位置
     */
    Integer pageStart;
    /**
     * 页大小
     */
    Integer pageSize;

    /**
     * 0：MySQL、1：postgreSQL
     */
    Integer typeOfDatabase = 0;
}
