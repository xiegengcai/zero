package com.xiegengcai.zero.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * <pre>
 *    在服务类中标该类，以便确定服务方法所属的组及相关信息。由于SpiBean已经标注了Spring的{@link Component}注解，因此标注了{@link ZeroBean}的类自动成为Spring的Bean.
 * </pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/26.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ZeroBean {
    /**
     * 指令版本号
     * @return
     */
    byte version() default 0;
}
