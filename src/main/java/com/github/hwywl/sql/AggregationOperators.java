package com.github.hwywl.sql;

/**
 * @author huangwenyi
 * @date 2021年2月20日17:24:00
 * @description 常用聚合运算操作
 */
public enum AggregationOperators {
    DISTINCT_COUNT("DISTINCT_COUNT", " count(distinct {field}) as {alias} "),
    COUNT("COUNT", " count({field}) as {alias} "),
    SUM("SUM", " sum({field}) as {alias} "),
    AVG("AVG", " avg({field}) as {alias} "),
    MAX("MAX", " max({field}) as {alias} "),
    MIN("MIN", " min({field}) as {alias} ");

    private String name;
    private String template;

    AggregationOperators(String name, String template) {
        this.name = name;
        this.template = template;
    }

    public static String getSqlTemplate(String name) {
        for (AggregationOperators value : AggregationOperators.values()) {
            if (value.name.equals(name)) {
                return value.template;
            }
        }

        return null;
    }

    public static AggregationOperators getAggregationOperator(String name) {
        for (AggregationOperators value : AggregationOperators.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
