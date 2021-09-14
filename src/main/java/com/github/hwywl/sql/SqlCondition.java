package com.github.hwywl.sql;

import lombok.Data;

/**
 * @author huangwenyi
 * @date 2021/2/20 下午2:40
 * @description SQL属性条件
 */
@Data
public class SqlCondition {

    /**
     * 逻辑运算操作
     */
    private String logicalOperator;

    /**
     * 关系运算操作
     */
    private String relationOperator;

    /**
     * 属性
     */
    private String field;

    /**
     * 值
     */
    private String value;

    public SqlCondition(String logicalOperator, String relationOperator, String field, String value) {
        this.logicalOperator = logicalOperator;
        this.relationOperator = relationOperator;
        this.field = field;
        this.value = value;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }
}
