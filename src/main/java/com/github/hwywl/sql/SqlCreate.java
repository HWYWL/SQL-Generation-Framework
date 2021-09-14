package com.github.hwywl.sql;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.hwywl.config.Constant;
import com.github.hwywl.model.Aggregation;
import com.github.hwywl.model.Condition;
import com.github.hwywl.model.Limit;
import com.github.hwywl.model.OrderBy;

import java.util.*;

/**
 * @author huangwenyi
 * @date 2021/2/20 上午9:15
 * @description
 */
public class SqlCreate {

    /**
     * sql语句的String类型
     */
    private StringBuffer sql = new StringBuffer();

    /**
     * 要操作的字段,用在update insert中的Set之后,字段名称和值进行对应
     */
    private Map<String, Object> operateFields = new HashMap<>();

    /**
     * 要操作的值 ,同于insert语句中的values,不需要跟字段名称,只需要值就OK了
     */
    private ArrayList<Object> operateValues = new ArrayList<>();

    /**
     * select语句中用于select 后面的字段名称
     */
    private ArrayList<String> fields = new ArrayList<>();

    /**
     * 条件集合,用于where语句后面 形式例如 field1=value1
     */
    private ArrayList<SqlCondition> conditions = new ArrayList<>();

    /**
     * 条件集合,用于where语句后面 形式例如 field1=value1
     */
    private ArrayList<SqlCondition> joinConditions = new ArrayList<>();

    /**
     * 操作符,使用select update delete insert者四种操作符
     */
    private String operate = "";

    /**
     * 操作表 ,要操作的表
     */
    private String table = "";

    /**
     * 限制 要限制的长度
     */
    private String limit = "";

    /**
     * 排序规则,定制排序规则，SortMethod
     */
    private String order = "";

    private String group = "";

    /**
     * 默认构造方法使用select语句操作
     */
    public SqlCreate() {
        this.operate = SqlOperate.SELECT;
    }

    public static SqlCreate create() {
        return new SqlCreate();
    }

    /**
     * 初始化构造数据库操作,默认使用select查询
     */
    public SqlCreate(String table) {
        this.table = table;
        this.operate = SqlOperate.SELECT;
    }

    public SqlCreate(String operate, String table) {
        this.operate = operate;
        this.table = table;
    }

    public SqlCreate operate(String operate) {
        this.operate = operate;
        return this;
    }

    /**
     * 主要应用update和insert的数据库操作添加sql
     */
    public SqlCreate operateFiled(String properName, Object properValue) {
        operateFields.put(properName, properValue);
        return this;
    }

    public SqlCreate operateFiled(Map<String, Object> fields) {
        operateFields.putAll(fields);
        return this;
    }

    public SqlCreate operateFiled(Object properValue) {
        operateValues.add(properValue);
        return this;
    }

    /**
     * 主要应用于select语句中，用于添加抬头字段
     *
     * @param @filed 字段
     * @return
     */
    public SqlCreate field(String field) {
        this.fields.add(field);
        return this;
    }

    /**
     * 主要应用于select语句中，用于添加抬头字段,聚合计算
     *
     * @param @filed 字段
     * @return
     */
    public SqlCreate aggregation(String field, AggregationOperators operator) {
        String agg = StrUtil.format(operator.getTemplate(), Dict.create().set("field", field).set("alias", operator.getName()));
        this.fields.add(agg);
        return this;
    }

    /**
     * 主要应用于select语句中，用于添加抬头字段,聚合计算
     *
     * @param field    字段
     * @param operator 连接符
     * @param alias    别名
     * @return
     */
    public SqlCreate aggregation(String field, AggregationOperators operator, String alias) {
        String agg = StrUtil.format(operator.getTemplate(), Dict.create().set("field", field).set("alias", alias));
        this.fields.add(agg);
        return this;
    }

    /**
     * 多个聚合条件解析
     *
     * @param aggregations 集合条件
     * @param tableAlias   表的别名
     * @return
     */
    public SqlCreate aggregations(List<Aggregation> aggregations, String tableAlias) {
        if (aggregations == null || aggregations.isEmpty()) {
            return this;
        }

        for (Aggregation value : aggregations) {
            String alias = value.getAlias();
            // 如果没传，则自动生成别名
            if (StrUtil.isEmpty(alias)) {
                String attribute = value.getField();
                String[] attributes = attribute.split("\\.");
                // 去除聚合中存在的别名
                attribute = attributes.length > 1 ? attributes[1] : attribute;
                // 使用表别名+下划线+聚合方式+下划线+字段名称组成聚合运算的别名
                alias = tableAlias + StrUtil.UNDERLINE + value.getAggregationMode() + StrUtil.UNDERLINE + attribute;
            }
            AggregationOperators aggregationOperator = AggregationOperators.getAggregationOperator(value.getAggregationMode());
            if (aggregationOperator != null) {
                aggregation(value.getField(), aggregationOperator, Constant.STRING_QUOTE + alias + Constant.STRING_QUOTE);
            }
        }
        return this;
    }

    /**
     * 主要应用于select语句中，用于添加抬头字段，一次增加多个fields,一次添加
     *
     * @param @fields 字段
     * @return
     */
    public SqlCreate fieldAll(String fields) {
        String[] fieldArray = fields.split(",");
        Collections.addAll(this.fields, fieldArray);
        return this;
    }

    /**
     * 主要应用于select语句中，用于添加抬头字段，一次增加多个fields,分开添加
     *
     * @param @field 字段
     * @return
     */
    public SqlCreate fields(String... field) {
        Collections.addAll(this.fields, field);
        return this;
    }

    /**
     * 添加字段并可以给字段加别名
     *
     * @param field 字段
     * @param alias 别名
     * @return
     */
    public SqlCreate field(String field, String alias) {
        this.fields.add(field + " as " + alias);
        return this;
    }

    /**
     * 相同子查詢不使用前面的别名
     *
     * @return
     */
    public SqlCreate table() {
        return this;
    }

    /**
     * 添加表名
     *
     * @param table 表名
     * @return
     */
    public SqlCreate table(String table) {
        this.table = table;
        return this;
    }

    /**
     * 添加字段
     *
     * @param table 表名
     * @param alias 别名
     * @return
     */
    public SqlCreate tableAlias(String table, String alias) {
        this.table += table + " as " + alias + " ";
        return this;
    }

    /**
     * 给结果集加上别名,可用于子查询
     *
     * @param resultOrTable 结果集/表名
     * @param alias         别名
     * @param joinType      多表连接类型
     * @return
     */
    public SqlCreate join(String resultOrTable, String alias, JoinOperators joinType) {
        if (StrUtil.isNotEmpty(resultOrTable) || joinType != null) {
            if (joinType == JoinOperators.Query_Subsystem) {
                if (StrUtil.isNotEmpty(this.table)) {
                    this.table += " ,(" + resultOrTable + ") " + " as " + alias;
                } else {
                    this.table += " (" + resultOrTable + ") " + " as " + alias;
                }
            } else {
                if (StrUtil.isNotEmpty(this.table)) {
                    this.table += " " + joinType + " join (" + resultOrTable + ") " + " as " + alias + assembleJoinCondition();
                } else {
                    this.table += " (" + resultOrTable + ") " + " as " + alias + assembleJoinCondition();
                }
            }
        }

        return this;
    }

    /**
     * 给结果集加上别名,可用于子查询
     *
     * @param jc 多表连接条件
     * @return
     */
    public SqlCreate joinCondition(List<Condition> jc) {
        if (!CollUtil.isEmpty(jc)) {
            for (Condition value : jc) {
                // 0：连接符，1：字段属性，2：字段值，3：运算符
                andJoinCondition(value.getAboveConditions(), value.getField(), value.getMiddleConditions(), value.getFieldValue());
            }
        }

        return this;
    }

    /**
     * 如果value不存在，则不添加
     *
     * @param field    字段
     * @param operator 操作符
     * @param value    值
     * @return
     */
    public SqlCreate andCondition(String field, String operator, Object value) {
        if (!ObjectUtil.isEmpty(value)) {
            andCondition(LogicalOperators.AND, operator, field, filterValue(value));
        }
        return this;
    }

    /**
     * 如果value不存在，则不添加，只能用于Join查询
     *
     * @param field    字段
     * @param operator 操作符
     * @param value    值
     * @return
     */
    public SqlCreate andJoinCondition(String field, String operator, Object value) {
        if (!ObjectUtil.isEmpty(value)) {
            andJoinCondition(LogicalOperators.AND, operator, field, filterValue(value));
        }
        return this;
    }

    /**
     * 如果value不存在，则不添加
     *
     * @param logical  与前面条件的连接符
     * @param field    字段
     * @param operator 操作符
     * @param value    值
     * @return
     */
    public SqlCreate andCondition(String logical, String field, String operator, Object value) {
        if (!ObjectUtil.isEmpty(value)) {
            if (operator.toLowerCase().equals(RelationalOperators.IN)) {
                conditionIn(logical, RelationalOperators.IN, field, Arrays.asList(value.toString().split(StrUtil.COMMA)));
            } else if (operator.toLowerCase().equals(RelationalOperators.NOT_IN)) {
                conditionIn(logical, RelationalOperators.NOT_IN, field, Arrays.asList(value.toString().split(StrUtil.COMMA)));
            } else if (operator.toLowerCase().equals(RelationalOperators.BETWEEN)) {
                conditionBetween(logical, RelationalOperators.BETWEEN, field, Arrays.asList(value.toString().split(StrUtil.COMMA)));
            } else {
                this.conditions.add(new SqlCondition(logical, operator, field, filterValue(value)));
            }
        } else if (operator.toLowerCase().equals(RelationalOperators.IS_NOT_NULL) ||
                operator.toLowerCase().equals(RelationalOperators.IS_NULL)) {
            this.conditions.add(new SqlCondition(logical, operator, field, ""));
        }
        return this;
    }

    /**
     * 如果value不存在，则不添加,只能用于join查询
     *
     * @param logical  与前面条件的连接符
     * @param field    字段
     * @param operator 操作符
     * @param value    值
     * @return
     */
    public SqlCreate andJoinCondition(String logical, String field, String operator, Object value) {
        if (!ObjectUtil.isEmpty(value)) {
            if (operator.toLowerCase().equals(RelationalOperators.IN)) {
                joinConditionIn(logical, RelationalOperators.IN, field, Arrays.asList(value.toString().split(StrUtil.COMMA)));
            } else if (operator.toLowerCase().equals(RelationalOperators.NOT_IN)) {
                joinConditionIn(logical, RelationalOperators.NOT_IN, field, Arrays.asList(value.toString().split(StrUtil.COMMA)));
            } else if (operator.toLowerCase().equals(RelationalOperators.BETWEEN)) {
                joinConditionBetween(logical, RelationalOperators.BETWEEN, field, Arrays.asList(value.toString().split(StrUtil.COMMA)));
            } else {
                this.joinConditions.add(new SqlCondition(logical, operator, field, filterValue(value)));
            }
        }
        return this;
    }

    /**
     * 多个查询条件，如果value不存在，则不添加
     *
     * @param conditions 查询条件拼接
     * @RETURN
     */
    public SqlCreate andConditions(List<Condition> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return this;
        }

        for (Condition value : conditions) {
            // 0：连接符，1：字段属性，2：字段值，3：运算符
            andCondition(value.getAboveConditions(), value.getField(), value.getMiddleConditions(), value.getFieldValue());
        }

        return this;
    }

    /**
     * 多个查询条件，如果value不存在，则不添加
     *
     * @param field    字符
     * @param operator 连接符
     * @param value    值
     * @return
     */
    public SqlCreate orCondition(String field, String operator, Object value) {
        if (!ObjectUtil.isEmpty(value)) {
            this.conditions.add(new SqlCondition(LogicalOperators.OR, operator, field, filterValue(value)));
        }
        return this;
    }

    /**
     * like查询连接符
     *
     * @param field 字段
     * @param value 值
     * @return 构造器
     */
    public SqlCreate andLike(String field, String value) {
        if (!ObjectUtil.isEmpty(value)) {
            value = "%" + value + "%";
            andCondition(field, "like", value);
        }
        return this;
    }

    /**
     * or查询连接符
     *
     * @param field 字段
     * @param value 值
     * @return 构造器
     */
    public SqlCreate orLike(String field, String value) {
        if (!ObjectUtil.isEmpty(value)) {
            value = "%" + value + "%";
            orCondition(field, "like", value);
        }
        return this;
    }

    /**
     * 如果value不存在，则不添加
     * 一种条件场景 where id in (1,2,3) or where id not in (1,2,3)等
     *
     * @param field  字段
     * @param values 值
     * @return
     */
    public <T> SqlCreate andConditionIn(String field, List<T> values) {
        return conditionIn(LogicalOperators.AND, RelationalOperators.IN, field, values);
    }

    public <T> SqlCreate orConditionIn(String field, List<T> values) {
        return conditionIn(LogicalOperators.OR, RelationalOperators.IN, field, values);
    }

    public <T> SqlCreate andConditionNotIn(String field, List<T> values) {
        return conditionIn(LogicalOperators.AND, RelationalOperators.NOT_IN, field, values);
    }

    public <T> SqlCreate orConditionNotIn(String field, List<T> values) {
        return conditionIn(LogicalOperators.OR, RelationalOperators.NOT_IN, field, values);
    }

    private <T> SqlCreate conditionIn(String logicalOperators, String relationalOperators, String field, List<T> values) {
        if (!values.isEmpty()) {
            String build = inConditionBuild(values);
            if (StrUtil.isNotEmpty(build)) {
                this.conditions.add(new SqlCondition(logicalOperators, relationalOperators, field, build));
            }
        }
        return this;
    }

    private <T> SqlCreate conditionBetween(String logicalOperators, String relationalOperators, String field, List<T> values) {
        if (!values.isEmpty()) {
            String build = betweenConditionBuild(values);
            if (StrUtil.isNotEmpty(build)) {
                this.conditions.add(new SqlCondition(logicalOperators, relationalOperators, field, build));
            }
        }
        return this;
    }

    private <T> SqlCreate joinConditionIn(String logicalOperators, String relationalOperators, String field, List<T> values) {
        if (!values.isEmpty()) {
            String build = inConditionBuild(values);
            if (StrUtil.isNotEmpty(build)) {
                this.joinConditions.add(new SqlCondition(logicalOperators, relationalOperators, field, build));
            }
        }
        return this;
    }

    private <T> SqlCreate joinConditionBetween(String logicalOperators, String relationalOperators, String field, List<T> values) {
        if (!values.isEmpty()) {
            String build = betweenConditionBuild(values);
            if (StrUtil.isNotEmpty(build)) {
                this.joinConditions.add(new SqlCondition(logicalOperators, relationalOperators, field, build));
            }
        }
        return this;
    }

    /**
     * 构建in条件
     *
     * @param values 条件参数
     * @return
     */
    private <T> String inConditionBuild(List<T> values) {
        if (!values.isEmpty()) {
            StringBuilder conditionStr = new StringBuilder();
            conditionStr.append(" (");

            for (Object value : values) {
                conditionStr.append(filterValue(value)).append(" ,");
            }

            conditionStr.delete(conditionStr.length() - 1, conditionStr.length());

            conditionStr.append(") ");

            return conditionStr.toString();
        }

        return null;
    }

    /**
     * 构建between条件
     *
     * @param values 条件参数
     * @return
     */
    private <T> String betweenConditionBuild(List<T> values) {
        if (!values.isEmpty()) {
            StringBuilder conditionStr = new StringBuilder();
            conditionStr.append(" ");

            for (Object value : values) {
                conditionStr.append(filterValue(value)).append(" and ");
            }

            conditionStr.delete(conditionStr.length() - 4, conditionStr.length());

            conditionStr.append(" ");

            return conditionStr.toString();
        }

        return null;
    }

    /**
     * 过滤值方法，如果是String类型和Date类型添加单引号，不是直接返回原值
     *
     * @param value
     * @return
     */
    private String filterValue(Object value) {
        // 如果是字符串或者是不包含.号的(包含.号的是查询连接符字段)
        if (value instanceof String && !((String) value).contains(StrUtil.DOT) && !((String) value).contains("'")) {
            if (NumberUtil.isNumber(value.toString())) {
                return value.toString();
            } else {
                return "'" + value + "'";
            }
        } else if (value instanceof Date) {
            return "'" + value + "'";
        } else {
            return value.toString();
        }
    }

    /**
     * 排序
     *
     * @param orderBys 排序
     * @return
     */
    public SqlCreate orderBys(List<OrderBy> orderBys) {
        if (orderBys == null || orderBys.isEmpty()) {
            return this;
        }
        StringBuilder oby = new StringBuilder();
        for (OrderBy value : orderBys) {
            for (String field : value.getFields()) {
                oby.append(field).append(StrUtil.COMMA);
            }
            String ob = oby.substring(0, oby.length() - 1) + StrUtil.SPACE + value.getSort() + StrUtil.COMMA;
            oby.setLength(0);
            oby.append(ob);
        }

        order = oby.substring(0, oby.length() - 1);

        return this;
    }

    /**
     * 聚合
     *
     * @param fields 字段
     * @return
     */
    public SqlCreate groupBy(String... fields) {
        StringBuilder groupBy = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            groupBy.append(fields[i]);
            if (i != fields.length - 1) {
                groupBy.append(",");
            }
        }
        group = groupBy.toString();
        return this;
    }

    /**
     * 类似重写toString方法,返回sql结果
     */
    public String build() {
        sql.setLength(0);
        switch (operate) {
            case "select":
                return selectToSql();
            case "insert":
            case "update":
            case "delete":
            default:
                return null;
        }
    }

    /**
     * 添加字段
     * 例子：filed1,field2
     */
    private void assembleFiled() {
        if (fields.size() > 0) {
            //批量加字段
            for (String filed : this.fields) {
                //最后一个字段不加逗号
                if (!filed.equals(fields.get(fields.size() - 1))) {
                    sql = sql.append(" ").append(filed).append(",");
                } else {
                    sql = sql.append(" ").append(filed);
                }
            }
        } else {
            sql = sql.append(" *");
        }
    }

    /**
     * 添加条件
     * 例子：where condition1<condition2 and condition3>condition4
     */
    private void assembleCondition() {
        if (!conditions.isEmpty()) {
            boolean first = true;
            sql = sql.append(" where ");
            for (SqlCondition condition : conditions) {
                //判断是不是第一个
                if (first) {
                    sql.append(" ").append(condition.getField())
                            .append(" ").append(condition.getRelationOperator())
                            .append(" ").append(condition.getValue());
                    first = false;
                } else {
                    sql.append(" ").append(condition.getLogicalOperator())
                            .append(" ").append(condition.getField())
                            .append(" ").append(condition.getRelationOperator())
                            .append(" ").append(condition.getValue());
                }
            }
        }
    }

    /**
     * 添加条件，用于join查询拼接
     * 例子：on condition1<condition2 and condition3>condition4
     */
    private String assembleJoinCondition() {
        if (!joinConditions.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            builder.append(" on ");
            for (SqlCondition condition : joinConditions) {
                //判断是不是第一个
                if (first) {
                    builder.append(" ").append(condition.getField())
                            .append(" ").append(condition.getRelationOperator())
                            .append(" ").append(condition.getValue());
                    first = false;
                } else {
                    builder.append(" ").append(condition.getLogicalOperator())
                            .append(" ").append(condition.getField())
                            .append(" ").append(condition.getRelationOperator())
                            .append(" ").append(condition.getValue());
                }
            }

            joinConditions.clear();

            return builder.toString();
        }

        return " ";
    }

    /**
     * 添加排序
     * 例子 order by field DESC
     */
    private void assembleOrder() {
        if (!order.isEmpty()) {
            sql.append(" order by ").append(order);
        }
    }

    private void assembleGroup() {
        if (!group.isEmpty()) {
            sql.append(" group by ").append(group);
        }
    }

    /**
     * 按条件分页
     *
     * @param limitConditions 第一个参数为开始位置，第二个参数为行数
     * @return
     */
    public SqlCreate limit(Limit limitConditions) {
        if (limitConditions == null || limitConditions.getPageSize() == null || limitConditions.getPageStart() == null) {
            return this;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(StrUtil.SPACE).append("limit").append(StrUtil.SPACE);
        int typeOfDatabase = limitConditions.getTypeOfDatabase() == null ? 0 : limitConditions.getTypeOfDatabase();
        if (typeOfDatabase == 0) {
            // 例子 limit 0,10
            builder.append(limitConditions.getPageStart()).append(StrUtil.COMMA).append(limitConditions.getPageSize());
        } else if (typeOfDatabase == 1) {
            // 例子 0 offset 10
            builder.append(limitConditions.getPageSize()).append(StrUtil.SPACE).append("offset")
                    .append(StrUtil.SPACE).append(limitConditions.getPageStart());
        }
        limit = builder.toString();

        return this;
    }

    /**
     * 添加SET语句到sql变量中
     * 例子 SET key=value,key2=value2
     */
    private void assembleKeyValue() {
        if (!operateFields.isEmpty()) {
            sql = sql.append(" SET");

            for (String key : operateFields.keySet()) {
                Object value = operateFields.get(key);
                sql = sql.append(" ").append(key).append(" = ").append(filterValue(value)).append(",");
            }

            //删除最后一个逗号
            sql = sql.deleteCharAt(sql.length() - 1);
        }
    }

    /**
     * 分页
     */
    private void assembleLimit() {
        if (!limit.isEmpty()) {
            sql.append(limit);
        }
    }

    /**
     * select语句转sql方法
     *
     * @return 返回转好的字符串
     */
    private String selectToSql() {
        sql = sql.append(SqlOperate.SELECT);
        //字段处理，没有字段默认使用*
        assembleFiled();
        //表处理，暂时单表
        sql = sql.append(" from ").append(table);
        //where条件处理,有处理，无不处理
        assembleCondition();
        //分组语句
        assembleGroup();
        //排序语句
        assembleOrder();
        // 分页
        assembleLimit();

        return sql.toString();
    }
}
