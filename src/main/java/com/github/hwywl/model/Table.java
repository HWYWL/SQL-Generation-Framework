package com.github.hwywl.model;

import lombok.*;

/**
 * @author: YI
 * @description: 表的名称和别名
 * @date: create in 2021/3/8 17:38
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Table {
    /**
     * 表名称
     */
    String tableName;
    /**
     * 表的别名
     */
    String alias;
}
