package com.xiegengcai.zero.annotation;

import java.lang.annotation.*;

/**
 * <pre>
 *     使用该注解对服务方法进行标注
 * </pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/26.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SPI {

    /**
     * 指令号
     * @return
     */
    int cmd();

    /**
     * 指令版本号
     * @return
     */
    byte version() default 1;

    /**
     * 服务方法是否已经过期，默认不过期
     */
    ObsoletedType obsoleted() default ObsoletedType.NO;
}
