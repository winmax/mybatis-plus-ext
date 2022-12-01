package com.tangzc.mpe.autotable.strategy.mysql.builder;

import com.tangzc.mpe.autotable.annotation.enums.IndexTypeEnum;
import com.tangzc.mpe.autotable.strategy.mysql.data.ColumnParam;
import com.tangzc.mpe.autotable.strategy.mysql.data.IndexParam;
import com.tangzc.mpe.autotable.strategy.mysql.data.TableParam;
import com.tangzc.mpe.autotable.utils.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author don
 */
@Slf4j
public class CreateTableSqlBuilder {

    /**
     * 构建创建新表的SQL
     *
     * @param tableParam 参数
     * @return sql
     */
    public static String buildSql(TableParam tableParam) {

        List<String> primaries = new ArrayList<>();

        String name = tableParam.getName();
        List<ColumnParam> columnParamList = tableParam.getColumnParamList();
        List<IndexParam> indexParamList = tableParam.getIndexParamList();
        String collate = tableParam.getCollate();
        String engine = tableParam.getEngine();
        String characterSet = tableParam.getCharacterSet();
        String comment = tableParam.getComment();

        // 记录所有修改项，（利用数组结构，便于添加,分割）
        List<String> addItems = new ArrayList<>();

        // 表字段处理
        addItems.add(
                columnParamList.stream().map(columnData -> {
                    // 判断是主键，自动设置为NOT NULL，并记录
                    if (columnData.isPrimary()) {
                        columnData.setNotNull(true);
                        primaries.add(columnData.getName());
                    }

                    // 拼接每个字段的sql片段
                    return columnData.toColumnSql();
                }).collect(Collectors.joining(","))
        );


        // 主键
        if (!primaries.isEmpty()) {
            String primaryKeySql = getPrimaryKeySql(primaries);
            addItems.add(primaryKeySql);
        }

        // 索引
        addItems.add(
                indexParamList.stream()
                        // 例子： UNIQUE INDEX `unique_name_age`(`name` ASC, `age` DESC) COMMENT '姓名、年龄索引' USING BTREE
                        .map(CreateTableSqlBuilder::getIndexSql)
                        // 同类型的索引，排在一起，SQL美化
                        .sorted()
                        .collect(Collectors.joining(","))
        );

        List<String> tableProperties = new ArrayList<>();

        // 引擎
        if (!StringUtils.isEmpty(engine)) {
            tableProperties.add("ENGINE = " + engine);
        }
        // 字符集
        if (!StringUtils.isEmpty(characterSet)) {
            tableProperties.add("CHARACTER SET = " + characterSet);
        }
        // 排序
        if (!StringUtils.isEmpty(collate)) {
            tableProperties.add("COLLATE = " + collate);
        }
        // 备注
        if (StringUtils.hasText(comment)) {
            tableProperties.add("COMMENT = '{comment}'"
                    .replace("{comment}", comment)
            );
        }

        // 组合sql: 过滤空字符项，逗号拼接
        String addSql = addItems.stream()
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(","));
        String propertiesSql = tableProperties.stream()
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(","));

        return "CREATE TABLE `{tableName}` ({addItems}) {tableProperties};"
                .replace("{tableName}", name)
                .replace("{addItems}", addSql)
                .replace("{tableProperties}", propertiesSql);
    }

    public static String getIndexSql(IndexParam indexParam) {
        // 例子： UNIQUE INDEX `unique_name_age`(`name` ASC, `age` DESC) COMMENT '姓名、年龄索引' USING BTREE,
        return StringHelper.newInstance("{indexType} INDEX `{indexName}`({columns}) {indexFunction} {indexComment}")
                .replace("{indexType}", indexParam.getType() == IndexTypeEnum.NORMAL ? "" : indexParam.getType().name())
                .replace("{indexName}", indexParam.getName())
                .replace("{columns}", (key) -> {
                    List<IndexParam.IndexColumnParam> columnParams = indexParam.getColumns();
                    return columnParams.stream().map(column ->
                            // 例：`name` ASC
                            "`{column}` {sortMode}"
                                    .replace("{column}", column.getColumn())
                                    .replace("{sortMode}", column.getSort() != null ? column.getSort().name() : "")
                    ).collect(Collectors.joining(","));
                })
                .replace("{indexFunction}", (key) -> {
                    if (indexParam.getFunction() != null) {
                        return "USING " + indexParam.getFunction().name();
                    } else {
                        return "";
                    }
                })
                .replace("{indexComment}", StringUtils.hasText(indexParam.getComment()) ? "COMMENT '" + indexParam.getComment() + "'" : "")
                .toString();
    }

    public static String getPrimaryKeySql(List<String> primaries) {
        return "PRIMARY KEY ({primaries})"
                .replace(
                        "{primaries}",
                        primaries.stream()
                                .map(fieldName -> "`" + fieldName + "`")
                                .collect(Collectors.joining(","))
                );
    }
}