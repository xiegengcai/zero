package com.xiegengcai.zero.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * <pre>
 *     过滤器注解
 * </pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/9/2.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ZeroFilter {
    /**
     * 顺序。值越小越先执行before方法，越晚执行after方法
     * @return
     */
    int order() default 0;

    /**
     * 拦截指令列表
     * @return
     */
    int [] cmds() default {};

    /**
     * 忽略拦截指令列表
     * @return
     */
    int [] ignoreCmds() default {};

}
