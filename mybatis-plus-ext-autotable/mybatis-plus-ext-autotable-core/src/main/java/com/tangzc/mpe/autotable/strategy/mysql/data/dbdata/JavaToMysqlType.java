package com.tangzc.mpe.autotable.strategy.mysql.data.dbdata;

import com.tangzc.mpe.autotable.strategy.mysql.data.enums.MySqlColumnTypeEnum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author don
 */
public class JavaToMysqlType {
    public static final Map<Class<?>, MySqlColumnTypeEnum> JAVA_TO_MYSQL_TYPE_MAP = new HashMap<Class<?>, MySqlColumnTypeEnum>() {{
        put(String.class, MySqlColumnTypeEnum.VARCHAR);
        put(Character.class, MySqlColumnTypeEnum.CHAR);
        put(char.class, MySqlColumnTypeEnum.CHAR);

        put(BigInteger.class, MySqlColumnTypeEnum.BIGINT);
        put(Long.class, MySqlColumnTypeEnum.BIGINT);
        put(long.class, MySqlColumnTypeEnum.BIGINT);

        put(Integer.class, MySqlColumnTypeEnum.INT);
        put(int.class, MySqlColumnTypeEnum.INT);

        put(Boolean.class, MySqlColumnTypeEnum.BIT);
        put(boolean.class, MySqlColumnTypeEnum.BIT);

        put(Float.class, MySqlColumnTypeEnum.FLOAT);
        put(float.class, MySqlColumnTypeEnum.FLOAT);
        put(Double.class, MySqlColumnTypeEnum.DOUBLE);
        put(double.class, MySqlColumnTypeEnum.DOUBLE);
        put(BigDecimal.class, MySqlColumnTypeEnum.DECIMAL);

        put(Date.class, MySqlColumnTypeEnum.DATETIME);
        put(java.sql.Date.class, MySqlColumnTypeEnum.DATE);
        put(java.sql.Timestamp.class, MySqlColumnTypeEnum.DATETIME);
        put(java.sql.Time.class, MySqlColumnTypeEnum.TIME);
        put(LocalDateTime.class, MySqlColumnTypeEnum.DATETIME);
        put(LocalDate.class, MySqlColumnTypeEnum.DATE);
        put(LocalTime.class, MySqlColumnTypeEnum.TIME);

        put(Short.class, MySqlColumnTypeEnum.SMALLINT);
        put(short.class, MySqlColumnTypeEnum.SMALLINT);
    }};

    public static MySqlColumnTypeEnum getSqlType(Class<?> fieldType) {

        // 枚举默认设置字符串类型
        if (fieldType.isEnum()) {
            return MySqlColumnTypeEnum.VARCHAR;
        }

        return JAVA_TO_MYSQL_TYPE_MAP.getOrDefault(fieldType, MySqlColumnTypeEnum.VARCHAR);
    }
}
