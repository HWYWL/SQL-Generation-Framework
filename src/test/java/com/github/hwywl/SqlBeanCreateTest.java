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
        List<String> joinFields = Arrays.asList("app_id", "platform_id", "order_id", "real_pay_amount");
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

    /**
     * 生成一条左连接查询的SQL
     * select overview_stat.statistics_date, overview_stat.app_id, overview_stat.platform_id, coin_stat.statistics_date,
     * coin_stat.app_id, coin_stat.platform_id,  sum(register_account) as "overview_stat_SUM_register_account" ,
     * sum(login_account) as "overview_stat_SUM_login_account" ,  sum(coin_sum) as "overview_stat_SUM_coin_sum" ,
     * sum(change_num) as "overview_stat_SUM_change_num"  from  (select statistics_date, app_id, platform_id,
     * sum(register_account) as "register_account" ,  sum(login_account) as "login_account"  from overview_stat as overview_stat
     * where  statistics_date >= '2021-09-22' group by statistics_date,app_id,platform_id)  as overview_stat
     * LEFT join (select statistics_date, app_id, platform_id,  sum(coin_sum) as "coin_sum" ,  sum(change_num) as "change_num"
     * from coin_stat as coin_stat  where  statistics_date >= '2021-09-22' group by statistics_date,app_id,platform_id)  as coin_stat
     * on  overview_stat.statistics_date = coin_stat.statistics_date and overview_stat.app_id = coin_stat.app_id and
     * overview_stat.platform_id = coin_stat.platform_id group by overview_stat.statistics_date,overview_stat.app_id,overview_stat.platform_id,coin_stat.statistics_date,coin_stat.app_id,coin_stat.platform_id
     */
    @Test
    public void createQueryLeftTest() {
        // SQL表,左连接如果和table的名称相同，则替换
        Table table = Table.builder().tableName("overview_stat").alias("overview_stat").build();

        // 表1聚合条件
        Aggregation agg1Table1 = Aggregation.builder().field("register_account").alias("register_account")
                .aggregationMode(AggregationOperators.SUM.getName()).build();
        Aggregation agg1Table2 = Aggregation.builder().field("login_account").alias("login_account")
                .aggregationMode(AggregationOperators.SUM.getName()).build();

        // 表1查询条件
        Condition condition1 = Condition.builder().aboveConditions(LogicalOperators.AND).field("statistics_date")
                .middleConditions(RelationalOperators.GE).fieldValue("2021-09-22").build();

        // 表1查询条件
        Join build1 = Join.builder().table(table).aggregation(Arrays.asList(agg1Table1, agg1Table2))
                .condition(Collections.singletonList(condition1)).fields(Arrays.asList("statistics_date", "app_id", "platform_id"))
                .groupBy(Arrays.asList("statistics_date", "app_id", "platform_id")).joinType(JoinOperators.Query_Subsystem.toString()).build();


        // 表2
        Table table2 = Table.builder().tableName("coin_stat").alias("coin_stat").build();
        // 表2聚合条件
        Aggregation agg2Table1 = Aggregation.builder().field("coin_sum").alias("coin_sum")
                .aggregationMode(AggregationOperators.SUM.getName()).build();
        Aggregation agg2Table2 = Aggregation.builder().field("change_num").alias("change_num")
                .aggregationMode(AggregationOperators.SUM.getName()).build();

        // 表2查询条件
        Condition condition2 = Condition.builder().aboveConditions(LogicalOperators.AND).field("statistics_date")
                .middleConditions(RelationalOperators.GE).fieldValue("2021-09-22").build();

        // 连接表条件
        Condition joinCondition1 = Condition.builder().aboveConditions(LogicalOperators.AND).field("overview_stat.statistics_date")
                .middleConditions(RelationalOperators.EQ).fieldValue("coin_stat.statistics_date").build();
        Condition joinCondition2 = Condition.builder().aboveConditions(LogicalOperators.AND).field("overview_stat.app_id")
                .middleConditions(RelationalOperators.EQ).fieldValue("coin_stat.app_id").build();
        Condition joinCondition3 = Condition.builder().aboveConditions(LogicalOperators.AND).field("overview_stat.platform_id")
                .middleConditions(RelationalOperators.EQ).fieldValue("coin_stat.platform_id").build();

        // 表2构造
        Join build2 = Join.builder().table(table2).aggregation(Arrays.asList(agg2Table1, agg2Table2))
                .condition(Collections.singletonList(condition2)).fields(Arrays.asList("statistics_date", "app_id", "platform_id"))
                .groupBy(Arrays.asList("statistics_date", "app_id", "platform_id")).joinType(JoinOperators.LEFT.toString())
                .joinCondition(Arrays.asList(joinCondition1, joinCondition2, joinCondition3)).build();


        Aggregation agg1 = Aggregation.builder().field("register_account")
                .aggregationMode(AggregationOperators.SUM.getName()).build();
        Aggregation agg2 = Aggregation.builder().field("login_account")
                .aggregationMode(AggregationOperators.SUM.getName()).build();
        Aggregation agg3 = Aggregation.builder().field("coin_sum")
                .aggregationMode(AggregationOperators.SUM.getName()).build();
        Aggregation agg4 = Aggregation.builder().field("change_num")
                .aggregationMode(AggregationOperators.SUM.getName()).build();

        QueryModel model = QueryModel.builder().table(table).aggregation(Arrays.asList(agg1, agg2, agg3, agg4))
                .joins(Arrays.asList(build1, build2))
                .groupBy(Arrays.asList("overview_stat.statistics_date", "overview_stat.app_id", "overview_stat.platform_id",
                        "coin_stat.statistics_date", "coin_stat.app_id", "coin_stat.platform_id")).build();
        String sql = JsonToSqlUtil.beanGeneratedSql(model);

        System.out.println(JSONUtil.toJsonStr(model));
        System.out.println(sql);
    }
}
