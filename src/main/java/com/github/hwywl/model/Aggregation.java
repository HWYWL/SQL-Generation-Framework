package com.github.hwywl.model;

import lombok.*;

/**
 * @author: YI
 * @description: SQL的聚合条件
 * @date: create in 2021/3/8 17:25
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Aggregation {
    /**
     * 属性名称
     */
    String field;

    /**
     * 聚合方式：
     * - DISTINCT_COUNT
     * - COUNT
     * - SUM
     * - AVG
     * - MAX
     * - MIN
     */
    String aggregationMode;

    /**
     * 别名
     */
    String alias;
}
