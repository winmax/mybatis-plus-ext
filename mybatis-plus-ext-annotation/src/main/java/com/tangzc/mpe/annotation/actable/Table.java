package com.tangzc.mpe.annotation.actable;


import com.tangzc.mpe.annotation.constants.MySqlCharsetConstant;
import com.tangzc.mpe.annotation.constants.MySqlEngineConstant;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 创建表时的表名
 *
 * @author sunchenbin
 * @version 2016年6月23日 下午6:13:37
 */
//表示注解加在接口、类、枚举等
@Target(ElementType.TYPE)
//VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息
@Retention(RetentionPolicy.RUNTIME)
//将此注解包含在javadoc中
@Documented
public @interface Table {

    /**
     * 表名
     *
     * @return 表名
     */
    String name() default "";

    /**
     * 表名
     *
     * @return 表名
     */
    String value() default "";

    /**
     * 表注释，也可以使用@TableComment注解代替
     *
     * @return
     */
    String comment() default "";

    /**
     * 表字符集，也可以使用@TableCharset注解代替
     * 仅支持com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant中的枚举字符集
     *
     * @return
     */
    MySqlCharsetConstant charset() default MySqlCharsetConstant.DEFAULT;

    /**
     * 表引擎，也可以使用@TableEngine注解代替
     * 仅支持com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant中的存储引擎枚举
     *
     * @return
     */
    MySqlEngineConstant engine() default MySqlEngineConstant.DEFAULT;

    /**
     * 需要排除的属性名，排除掉的属性不参与建表
     *
     * @return
     */
    String[] excludeFields() default {"serialVersionUID"};
}
