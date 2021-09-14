package com.github.hwywl.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.hwywl.model.*;
import com.github.hwywl.sql.JoinOperators;
import com.github.hwywl.sql.SqlCreate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: YI
 * @description: json生成sql语句
 * @date: 2021年9月14日11:31:03
 */
public class JsonToSqlUtil {
    /**
     * 接送生成SQL语句
     *
     * @param jsonStr 生成SQL的json结构条件
     * @return SQL
     */
    public static String jsonGeneratedSql(String jsonStr) throws NullPointerException {
        return beanGeneratedSql(JSONUtil.toBean(jsonStr, QueryModel.class));
    }

    /**
     * 接送生成SQL语句
     *
     * @param queryModel 生成SQL的bean结构条件
     * @return SQL
     */
    public static String beanGeneratedSql(QueryModel queryModel) throws NullPointerException {
        Table tableAlias = queryModel.getTable();
        SqlCreate resultSqlCreate = getSqlCreate(tableAlias, queryModel.getJoins(), queryModel.getCondition());

        List<Join> joins = queryModel.getJoins();

        // 如果groupBy不为空，则自动添加到需要的分组字段
        List<String> groupBy = queryModel.getGroupBy();
        if (CollUtil.isNotEmpty(groupBy)){
            queryModel.setFields(groupBy);
        }

        // 连表查询
        if (!CollUtil.isEmpty(joins)) {
            // 拼接多表查询SQL
            for (Join join : joins) {
                if (join != null && StrUtil.isNotEmpty(join.getJoinType())) {
                    String joinSql;
                    SqlCreate sqlCreate = SqlCreate.create();
                    // 分割表名和别名
                    Table joinTableAlias = join.getTable();
                    List<String> fields = join.getFields();
                    List<Aggregation> aggregations = join.getAggregation();
                    List<Condition> conditions = join.getCondition();
                    List<Condition> joinCondition = join.getJoinCondition();
                    if (CollUtil.isEmpty(fields)) {
                        joinSql = joinTableAlias.getTableName();
                    } else {
                        joinSql = sqlCreate.tableAlias(joinTableAlias.getTableName(), joinTableAlias.getAlias())
                                .fields(ArrayUtil.toArray(fields, String.class))
                                .aggregations(aggregations, joinTableAlias.getAlias())
                                .andConditions(conditions)
                                .groupBy(CollUtil.isEmpty(join.getGroupBy()) ? new String[0] : ArrayUtil.toArray(join.getGroupBy(), String.class))
                                .orderBys(join.getOrderBy())
                                .limit(queryModel.getLimit()).build();
                    }

                    resultSqlCreate = resultSqlCreate
                            .joinCondition(joinCondition)
                            .join(StrUtil.isEmpty(joinSql) ? null : joinSql,
                                    StrUtil.isEmpty(joinSql) ? null : joinTableAlias.getAlias(),
                                    StrUtil.isEmpty(joinSql) ? null : JoinOperators.valueOf(join.getJoinType()));
                }
            }
        }

        resultSqlCreate.fields(ArrayUtil.toArray(queryModel.getFields(), String.class))
                .aggregations(queryModel.getAggregation(), tableAlias.getAlias())
                .andConditions(queryModel.getCondition())
                .groupBy(queryModel.getGroupBy() == null || queryModel.getGroupBy().isEmpty() ?
                        new String[0] : ArrayUtil.toArray(queryModel.getGroupBy(), String.class))
                .orderBys(queryModel.getOrderBy())
                .limit(queryModel.getLimit()).build();

        return resultSqlCreate.build();
    }

    /**
     * 获取拼接SQL结构，子查询如果和table的名称和别名相同，则替换
     *
     * @param tableAlias 表的名称和别名
     * @param joins      子查询
     * @param condition  父条件
     * @return
     */
    private static SqlCreate getSqlCreate(Table tableAlias, List<Join> joins, List<Condition> condition) throws NullPointerException {
        SqlCreate resultSqlCreate = SqlCreate.create();

        if (CollUtil.isNotEmpty(joins)) {
            List<String> tableNames = joins.stream().map(e -> e.getTable().getTableName()).collect(Collectors.toList());
            List<String> alias = joins.stream().map(e -> e.getTable().getAlias()).collect(Collectors.toList());
            if (tableNames.contains(tableAlias.getTableName()) && alias.contains(tableAlias.getAlias())) {
                resultSqlCreate = resultSqlCreate.table();
            }
        } else {
            if (CollUtil.isEmpty(condition)) {
                throw new NullPointerException("连接条件不能为空!");
            }
            resultSqlCreate = resultSqlCreate.tableAlias(tableAlias.getTableName(), tableAlias.getAlias());
        }

        return resultSqlCreate;
    }
}
