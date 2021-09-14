package com.github.hwywl;

import com.github.hwywl.utils.JsonToSqlUtil;
import org.junit.Test;

/**
 * @author YI
 * @description json转SQL
 * @date create in 2021/9/14 11:49
 */
public class SqlJsonCreateTest {
    /**
     * 生成一条简单的SQL
     * select order_id, real_pay_amount from event_consumer_game_order_status as order_info  where  pay_status in  (1 ,2 )
     */
    @Test
    public void simpleCreateTest() {
        String jsonSQL = "{\"condition\":[{\"middleConditions\":\"in\",\"fieldValue\":\"1,2\",\"field\":\"pay_status\",\"aboveConditions\":\"and\"}],\"fields\":[\"order_id\",\"real_pay_amount\"],\"table\":{\"tableName\":\"event_consumer_game_order_status\",\"alias\":\"order_info\"}}";
        String sql = JsonToSqlUtil.jsonGeneratedSql(jsonSQL);
        System.out.println(sql);
    }

    /**
     * 生成一条简单的分页SQL
     * select order_id, real_pay_amount from event_consumer_game_order_status as order_info  where  pay_status in  (1 ,2 )  limit 10 offset 0
     */
    @Test
    public void simpleCreateLimitTest() {
        String jsonSQL = "{\"condition\":[{\"middleConditions\":\"in\",\"fieldValue\":\"1,2\",\"field\":\"pay_status\",\"aboveConditions\":\"and\"}],\"limit\":{\"pageSize\":10,\"pageStart\":0,\"typeOfDatabase\":1},\"fields\":[\"order_id\",\"real_pay_amount\"],\"table\":{\"tableName\":\"event_consumer_game_order_status\",\"alias\":\"order_info\"}}";
        String sql = JsonToSqlUtil.jsonGeneratedSql(jsonSQL);
        System.out.println(sql);
    }

    /**
     * 生成一条降序排序的SQL
     * select order_id, real_pay_amount from event_consumer_game_order_status as order_info  where
     * pay_status in  (1 ,2 )  order by pay_time,pay_status desc
     */
    @Test
    public void simpleCreateOrderByDescTest() {
        String jsonSQL = "{\"orderBy\":[{\"sort\":\"desc\",\"fields\":[\"pay_time\",\"pay_status\"]}],\"condition\":[{\"middleConditions\":\"in\",\"fieldValue\":\"1,2\",\"field\":\"pay_status\",\"aboveConditions\":\"and\"}],\"fields\":[\"order_id\",\"real_pay_amount\"],\"table\":{\"tableName\":\"event_consumer_game_order_status\",\"alias\":\"order_info\"}}";
        String sql = JsonToSqlUtil.jsonGeneratedSql(jsonSQL);
        System.out.println(sql);
    }

    /**
     * 生成一条正反排序的SQL
     * select order_id, real_pay_amount from event_consumer_game_order_status as order_info  where
     * pay_status in  (1 ,2 )  order by pay_time,pay_status desc,app_id,platform_id asc
     */
    @Test
    public void simpleCreateOrderByTest() {
        String jsonSQL = "{\"orderBy\":[{\"sort\":\"desc\",\"fields\":[\"pay_time\",\"pay_status\"]},{\"sort\":\"asc\",\"fields\":[\"app_id\",\"platform_id\"]}],\"condition\":[{\"middleConditions\":\"in\",\"fieldValue\":\"1,2\",\"field\":\"pay_status\",\"aboveConditions\":\"and\"}],\"fields\":[\"order_id\",\"real_pay_amount\"],\"table\":{\"tableName\":\"event_consumer_game_order_status\",\"alias\":\"order_info\"}}";
        String sql = JsonToSqlUtil.jsonGeneratedSql(jsonSQL);
        System.out.println(sql);
    }

    /**
     * 生成一条分组聚合的SQL
     * select app_id, platform_id,  count(distinct order_id) as "oc" ,  sum(real_pay_amount) as "ps"
     * from event_consumer_game_order_status as order_info  where  pay_status = 2 group by app_id,platform_id
     */
    @Test
    public void simpleCreateGroupByTest() {
        String jsonSQL = "{\"aggregation\":[{\"field\":\"order_id\",\"alias\":\"oc\",\"aggregationMode\":\"DISTINCT_COUNT\"},{\"field\":\"real_pay_amount\",\"alias\":\"ps\",\"aggregationMode\":\"SUM\"}],\"groupBy\":[\"app_id\",\"platform_id\"],\"condition\":[{\"middleConditions\":\"=\",\"fieldValue\":\"2\",\"field\":\"pay_status\",\"aboveConditions\":\"and\"}],\"fields\":[\"app_id\",\"platform_id\"],\"table\":{\"tableName\":\"event_consumer_game_order_status\",\"alias\":\"order_info\"}}";
        String sql = JsonToSqlUtil.jsonGeneratedSql(jsonSQL);
        System.out.println(sql);
    }

    /**
     * 生成一条子查询的SQL
     * select app_id, platform_id,  count(distinct order_id) as "oc" ,  sum(real_pay_amount) as "ps"  from
     * (select app_id, platform_id, order_id, real_pay_amount from event_consumer_game_order_status as join_gos  where  pay_status = 2)  as join_gos group by app_id,platform_id
     */
    @Test
    public void createQuerySubsystemTest() {
        String jsonSQL = "{\"joins\":[{\"condition\":[{\"middleConditions\":\"=\",\"fieldValue\":\"2\",\"field\":\"pay_status\",\"aboveConditions\":\"and\"}],\"joinType\":\"Query_Subsystem\",\"fields\":[\"app_id\",\"platform_id\",\"order_id\",\"real_pay_amount\"],\"table\":{\"tableName\":\"event_consumer_game_order_status\",\"alias\":\"join_gos\"}}],\"aggregation\":[{\"field\":\"order_id\",\"alias\":\"oc\",\"aggregationMode\":\"DISTINCT_COUNT\"},{\"field\":\"real_pay_amount\",\"alias\":\"ps\",\"aggregationMode\":\"SUM\"}],\"groupBy\":[\"app_id\",\"platform_id\"],\"fields\":[\"app_id\",\"platform_id\"],\"table\":{\"tableName\":\"event_consumer_game_order_status\"}}";
        String sql = JsonToSqlUtil.jsonGeneratedSql(jsonSQL);
        System.out.println(sql);
    }
}
