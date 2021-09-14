package com.github.hwywl.model;

import lombok.*;

/**
 * @author: YI
 * @description: SQL的连接条件
 * @date: create in 2021/3/8 17:08
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Condition {
    /**
     * 属性名称
     */
    String field;

    /**
     * 与上一条语句的连接符
     */
    String aboveConditions;

    /**
     * 属性名称和属性值之间的连接符
     */
    String middleConditions;

    /**
     * 属性的值
     */
    String fieldValue;
}
