package com.github.hwywl;

import cn.hutool.json.JSONUtil;
import com.github.hwywl.model.*;
import com.github.hwywl.sql.*;
import com.github.hwywl.utils.JsonToSqlUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author YI
 * @description bean转SQL
 * @date create in 2021/9/14 11:49
 */
public class SqlBeanCreateTest {
    /**
     * 生成一条简单的SQL
     * select order_id, real_pay_amount from event_consumer_game_order_status as order_info  where  pay_status in  (1 ,2 )
     */
    @Test
    public void simpleCreateTest() {
        // SQL表
        Table table = Table.builder().tableName("event_consumer_game_order_status").alias("order_info").build();
        // 现需要获取的字段
        List<String> fields = Arrays.asList("order_id", "real_pay_amount");

        // SQL条件
        Condition condition = Condition.builder().aboveConditions(LogicalOperators.AND).field("pay_status")
                .middleConditions(RelationalOperators.IN).fieldValue("1,2").build();
        List<Condition> conditions = Collections.singletonList(condition);

        QueryModel model = QueryModel.builder().table(table).fields(fields).condition(conditions).build();
        String sql = JsonToSqlUtil.beanGeneratedSql(model);

        System.out.println(JSONUtil.toJsonStr(model));
        System.out.println(sql);
    }

    /**
     * 生成一条简单的分页SQL
     * select order_id, real_pay_amount from event_consumer_game_order_status as order_info  where  pay_status between  1 and 2   limit 10 offset 0
     */
    @Test
    public void simpleCreateLimitTest() {
        // SQL表
        Table table = Table.builder().tableName("event_consumer_game_order_status").alias("order_info").build();
        // 现需要获取的字段
        List<String> fields = Arrays.asList("order_id", "real_pay_amount");

        // SQL条件
        Condition condition = Condition.builder().aboveConditions(LogicalOperators.AND).field("pay_status")
                .middleConditions(RelationalOperators.BETWEEN).fieldValue("1,2").build();
        List<Condition> conditions = Collections.singletonList(condition);

        // 由于不同数据库分页都不同，目前支持了两种分页，0：MySQL、1：postgreSQL
        Limit limit = Limit.builder().pageStart(0).pageSize(10).typeOfDatabase(1).build();

        QueryModel model = QueryModel.builder().table(table).fields(fields).condition(conditions).limit(limit).build();
        String sql = JsonToSqlUtil.beanGeneratedSql(model);

        System.out.println(JSONUtil.toJsonStr(model));
        System.out.println(sql);
    }

    /**
     * 生成一条降序排序的SQL
     * select order_id, real_pay_amount from event_consumer_game_order_status as order_info  where
     * pay_status in  (1 ,2 )  order by pay_time,pay_status desc
     */
    @Test
    public void simpleCreateOrderByDescTest() {
        // SQL表
        Table table = Table.builder().tableName("event_consumer_game_order_status").alias("order_info").build();
        // 现需要获取的字段
        List<String> fields = Arrays.asList("order_id", "real_pay_amount");

        // SQL条件
        Condition condition = Condition.builder().aboveConditions(LogicalOperators.AND).field("pay_status")
                .middleConditions(RelationalOperators.IN).fieldValue("1,2").build();
        List<Condition> conditions = Collections.singletonList(condition);

        // 排序
        OrderBy desc = OrderBy.builder().fields(Arrays.asList("pay_time", "pay_status")).sort(SortMethod.DESC).build();
        List<OrderBy> orderBys = Collections.singletonList(desc);

        QueryModel model = QueryModel.builder().table(table).fields(fields).condition(conditions).orderBy(orderBys).build();
        String sql = JsonToSqlUtil.beanGeneratedSql(model);

        System.out.println(JSONUtil.toJsonStr(model));
        System.out.println(sql);
    }

    /**
     * 生成一条正反排序的SQL
     * select order_id, real_pay_amount from event_consumer_game_order_status as order_info  where
     * pay_status in  (1 ,2 )  order by pay_time,pay_status desc,app_id,platform_id asc
     */
    @Test
    public void simpleCreateOrderByTest() {
        // SQL表
        Table table = Table.builder().tableName("event_consumer_game_order_status").alias("order_info").build();
        // 现需要获取的字段
        List<String> fields = Arrays.asList("order_id", "real_pay_amount");

        // SQL条件
        Condition condition = Condition.builder().aboveConditions(LogicalOperators.AND).field("pay_status")
                .middleConditions(RelationalOperators.IN).fieldValue("1,2").build();
        List<Condition> conditions = Collections.singletonList(condition);

        // 排序
        OrderBy desc = OrderBy.builder().fields(Arrays.asList("pay_time", "pay_status")).sort(SortMethod.DESC).build();
        OrderBy asc = OrderBy.builder().fields(Arrays.asList("app_id", "platform_id")).sort(SortMethod.ASC).build();
        List<OrderBy> orderBys = Arrays.asList(desc, asc);

        QueryModel model = QueryModel.builder().table(table).fields(fields).condition(conditions).orderBy(orderBys).build();
        String sql = JsonToSqlUtil.beanGeneratedSql(model);

        System.out.println(JSONUtil.toJsonStr(model));
        System.out.println(sql);
    }

    /**
     * 生成一条分组聚合的SQL
     * select app_id, platform_id,  count(distinct order_id) as "oc" ,  sum(real_pay_amount) as "ps"
     * from event_consumer_game_order_status as order_info  where  pay_status = 2 group by app_id,platform_id
     */
    @Test
    public void simpleCreateGroupByTest() {
        // SQL表
        Table table = Table.builder().tableName("event_consumer_game_order_status").alias("order_info").build();

        // 聚合运算
        Aggregation orderId = Aggregation.builder().field("order_id")
                .aggregationMode(AggregationOperators.DISTINCT_COUNT.getName()).alias("oc").build();
        Aggregation realPayAmount = Aggregation.builder().field("real_pay_amount")
                .aggregationMode(AggregationOperators.SUM.getName()).alias("ps").build();
        List<Aggregation> aggregations = Arrays.asList(orderId, realPayAmount);

        // SQL条件
        Condition condition = Condition.builder().aboveConditions(LogicalOperators.AND).field("pay_status")
                .middleConditions(RelationalOperators.EQ).fieldValue("2").build();
        List<Condition> conditions = Collections.singletonList(condition);

        // 分组
        List<String> groupBys = Arrays.asList("app_id", "platform_id");

        QueryModel model = QueryModel.builder().table(table).aggregation(aggregations)
                .condition(conditions).groupBy(groupBys).build();
        String sql = JsonToSqlUtil.beanGeneratedSql(model);

        System.out.println(JSONUtil.toJsonStr(model));
        System.out.println(sql);
    }

    /**
     * 生成一条子查询的SQL
     * select app_id, platform_id,  count(distinct order_id) as "oc" ,  sum(real_pay_amount) as "ps"  from
     * (select app_id, platform_id, order_id, real_pay_amount from event_consumer_game_order_status as join_gos  where  pay_status = 2)  as join_gos group by app_id,platform_id
     */
    @Test
    public void createQuerySubsystemTest() {
        // SQL表,子查询如果和table的名称相同，则替换
        Table table = Table.builder().tableName("event_consumer_game_order_status").build();

        // 聚合运算
        Aggregation orderId = Aggregation.builder().field("order_id")
                .aggregationMode(AggregationOperators.DISTINCT_COUNT.getName()).alias("oc").build();
        Aggregation realPayAmount = Aggregation.builder().field("real_pay_amount")
                .aggregationMode(AggregationOperators.SUM.getName()).alias("ps").build();
        List<Aggregation> aggregations = Arrays.asList(orderId, realPayAmount);

        // 子查询的字段
        List<String> joinFields = Arrays.asList("app_id", "platform_id","order_id", "real_pay_amount");
        // 子查询 SQL表
        Table joinTable = Table.builder().tableName("event_consumer_game_order_status").alias("join_gos").build();
        // 子查询 SQL条件
        Condition condition = Condition.builder().aboveConditions(LogicalOperators.AND).field("pay_status")
                .middleConditions(RelationalOperators.EQ).fieldValue("2").build();
        List<Condition> joinConditions = Collections.singletonList(condition);
        Join join = Join.builder().table(joinTable).joinType(JoinOperators.Query_Subsystem.toString()).fields(joinFields)
                .condition(joinConditions).build();

        // 分组
        List<String> groupBys = Arrays.asList("app_id", "platform_id");

        QueryModel model = QueryModel.builder().table(table).aggregation(aggregations)
                .joins(Collections.singletonList(join)).groupBy(groupBys).build();
        String sql = JsonToSqlUtil.beanGeneratedSql(model);

        System.out.println(JSONUtil.toJsonStr(model));
        System.out.println(sql);
    }
}
