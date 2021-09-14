package com.github.hwywl.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * @author: YI
 * @description: SQL查询条件
 * @date: create in 2021/2/22 16:45
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class QueryModel {
    /**
     * 表名
     */
    @NotNull(message = "表名不能为空")
    private Table table;
    /**
     * 子/连接查询条件
     */
    private List<Join> joins;
    /**
     * 需要的维度
     */
    @NotEmpty(message = "维度不能为空")
    private List<String> fields;
    /**
     * 需要的聚合维度
     */
    private List<Aggregation> aggregation;
    /**
     * 条件
     */
    private List<Condition> condition;
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
