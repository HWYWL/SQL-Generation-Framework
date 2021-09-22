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

    /**
     * 生成一条左连接查询
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
        String jsonSQL = "{\"joins\":[{\"aggregation\":[{\"field\":\"register_account\",\"alias\":\"register_account\",\"aggregationMode\":\"SUM\"},{\"field\":\"login_account\",\"alias\":\"login_account\",\"aggregationMode\":\"SUM\"}],\"groupBy\":[\"statistics_date\",\"app_id\",\"platform_id\"],\"condition\":[{\"middleConditions\":\">=\",\"fieldValue\":\"2021-09-22\",\"field\":\"statistics_date\",\"aboveConditions\":\"and\"}],\"joinType\":\"Query_Subsystem\",\"fields\":[\"statistics_date\",\"app_id\",\"platform_id\"],\"table\":{\"tableName\":\"overview_stat\",\"alias\":\"overview_stat\"}},{\"aggregation\":[{\"field\":\"coin_sum\",\"alias\":\"coin_sum\",\"aggregationMode\":\"SUM\"},{\"field\":\"change_num\",\"alias\":\"change_num\",\"aggregationMode\":\"SUM\"}],\"groupBy\":[\"statistics_date\",\"app_id\",\"platform_id\"],\"joinCondition\":[{\"middleConditions\":\"=\",\"fieldValue\":\"coin_stat.statistics_date\",\"field\":\"overview_stat.statistics_date\",\"aboveConditions\":\"and\"},{\"middleConditions\":\"=\",\"fieldValue\":\"coin_stat.app_id\",\"field\":\"overview_stat.app_id\",\"aboveConditions\":\"and\"},{\"middleConditions\":\"=\",\"fieldValue\":\"coin_stat.platform_id\",\"field\":\"overview_stat.platform_id\",\"aboveConditions\":\"and\"}],\"condition\":[{\"middleConditions\":\">=\",\"fieldValue\":\"2021-09-22\",\"field\":\"statistics_date\",\"aboveConditions\":\"and\"}],\"joinType\":\"LEFT\",\"fields\":[\"statistics_date\",\"app_id\",\"platform_id\"],\"table\":{\"tableName\":\"coin_stat\",\"alias\":\"coin_stat\"}}],\"aggregation\":[{\"field\":\"register_account\",\"aggregationMode\":\"SUM\"},{\"field\":\"login_account\",\"aggregationMode\":\"SUM\"},{\"field\":\"coin_sum\",\"aggregationMode\":\"SUM\"},{\"field\":\"change_num\",\"aggregationMode\":\"SUM\"}],\"groupBy\":[\"overview_stat.statistics_date\",\"overview_stat.app_id\",\"overview_stat.platform_id\",\"coin_stat.statistics_date\",\"coin_stat.app_id\",\"coin_stat.platform_id\"],\"fields\":[\"overview_stat.statistics_date\",\"overview_stat.app_id\",\"overview_stat.platform_id\",\"coin_stat.statistics_date\",\"coin_stat.app_id\",\"coin_stat.platform_id\"],\"table\":{\"tableName\":\"overview_stat\",\"alias\":\"overview_stat\"}}\n";
        String sql = JsonToSqlUtil.jsonGeneratedSql(jsonSQL);
        System.out.println(sql);
    }
}
