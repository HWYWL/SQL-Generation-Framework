package com.github.hwywl.model;

import lombok.*;

import java.util.List;

/**
 * @author: YI
 * @description: 连接查询条件
 * @date: create in 2021/2/22 16:45
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Join {
    /**
     * 子/连接查询类型，INNER、LEFT、RIGHT、FULL、Query_Subsystem
     */
    private String joinType;
    /**
     * 需要连接的表
     */
    private Table table;
    /**
     * 需要的维度
     */
    private List<String> fields;
    /**
     * 需要的聚合纬度
     */
    private List<Aggregation> aggregation;
    /**
     * 条件
     */
    private List<Condition> condition;
    /**
     * join条件
     */
    private List<Condition> joinCondition;
    /**
     * 分组
     */
    private List<String> groupBy;
    /**
     * 排序
     */
    private List<OrderBy> orderBy;
    /**
     * 分页限制
     */
    private Limit limit;
}
