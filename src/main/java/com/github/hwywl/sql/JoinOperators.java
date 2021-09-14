package com.github.hwywl.sql;

/**
 * @author huangwenyi
 * @date 2021-2-22 15:14:23
 * @description 多表链接运算操作
 */
public enum JoinOperators {
    /**
     * 如果表中有至少一个匹配，则返回行
     */
    INNER,
    /**
     * 即使右表中没有匹配，也从左表返回所有的行
     */
    LEFT,
    /**
     * 即使左表中没有匹配，也从右表返回所有的行
     */
    RIGHT,
    /**
     * 只要其中一个表中存在匹配，就返回行
     */
    FULL,
    /**
     * 子查询标识
     */
    Query_Subsystem,
}
