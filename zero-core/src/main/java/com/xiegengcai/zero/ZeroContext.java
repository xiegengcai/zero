package com.xiegengcai.zero;

import com.google.protobuf.MessageLite;
import com.xiegengcai.zero.annotation.ObsoletedType;
import com.xiegengcai.zero.annotation.ZeroBean;
import com.xiegengcai.zero.annotation.ZeroFilter;
import com.xiegengcai.zero.annotation.SPI;
import com.xiegengcai.zero.exception.ZeroException;
import com.xiegengcai.zero.filter.Filter;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/26.
 */
@Component
public class ZeroContext implements BeanPostProcessor {
    private Map<String, SpiMethodHandler> spiMethodHandlerMap;
    private TreeSet<ZeroFilterValue> filters;


    public ZeroContext() {
        this.spiMethodHandlerMap = new HashMap<>();
        this.filters = new TreeSet<>();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (AnnotationUtils.findAnnotation(bean.getClass(), ZeroBean.class) != null) {
            ReflectionUtils.doWithMethods(bean.getClass(),
                    method -> {

                        //ZeroBean 方法的注解
                        SPI SPI = AnnotationUtils.findAnnotation(method, SPI.class);
                        //方法注解上的值
                        SpiMethodValue spiMethodValue = new SpiMethodValue(SPI.cmd(), SPI.version()
                                , ObsoletedType.isObsoleted(SPI.obsoleted()));
                        //处理方法的类
                        SpiMethodHandler spiMethodHandler = new SpiMethodHandler();
                        //serviceMethodValue
                        spiMethodHandler.setSpiMethodValue(spiMethodValue);
                        //handler
                        spiMethodHandler.setHandler(bean);
                        //method
                        spiMethodHandler.setHandlerMethod(method);
                        String handlerKey = buildeHandlerKey(spiMethodValue.getCmd(), spiMethodValue.getVersion());
                        //判断重复
                        if (spiMethodHandlerMap.get(handlerKey) != null) {
                            throw new ZeroException(
                                    new StringBuilder("重复的指令， ").append(handlerKey).toString());
                        }
                        // 判断返回类型
                        if (!ClassUtils.isAssignable(MessageLite.class, method.getReturnType())) {
                            throw new ZeroException("返回类型只能是MessageLite及其子类");
                        }
                        if (method.getParameterTypes().length > 3) {
                            throw new ZeroException(String.format("%s#%s最多包含三个个参数", method.getDeclaringClass().getCanonicalName(), method.getName()));
                        } else if (method.getParameterTypes().length == 1){
                            if (!(ClassUtils.isAssignable(long.class, method.getParameterTypes()[0])
                                    || ClassUtils.isAssignable(byte[].class, method.getParameterTypes()[0])
                                    || ClassUtils.isAssignable(ChannelHandlerContext.class, method.getParameterTypes()[0]))) {
                                throw new ZeroException(String.format("允许%s#%s(long)、(byte[])或(ChannelHandlerContext）", method.getDeclaringClass().getCanonicalName(), method.getName()));
                            }
                        } else if(method.getParameterTypes().length == 2){
                            boolean fail = true;
                            if (ClassUtils.isAssignable(int.class, method.getParameterTypes()[0])
                                    && (ClassUtils.isAssignable(byte[].class, method.getParameterTypes()[1])
                                    || ClassUtils.isAssignable(ChannelHandlerContext.class, method.getParameterTypes()[1]))) {
                                fail = false;
                            }
                            if (ClassUtils.isAssignable(byte[].class, method.getParameterTypes()[0])
                                    && (ClassUtils.isAssignable(int.class, method.getParameterTypes()[1])
                                    || ClassUtils.isAssignable(ChannelHandlerContext.class, method.getParameterTypes()[1]))) {
                                fail = false;
                            }
                            if (ClassUtils.isAssignable(ChannelHandlerContext.class, method.getParameterTypes()[0])
                                    && (ClassUtils.isAssignable(int.class, method.getParameterTypes()[1])
                                    || ClassUtils.isAssignable(byte[].class, method.getParameterTypes()[1]))) {
                                fail = false;
                            }
                            if (fail) {
                                throw new ZeroException(String.format("允许%s#%s(long, byte[])或(long, ChannelHandlerContext)、(byte[], long)或(byte[], ChannelHandlerContext)、(ChannelHandlerContext, long)或(ChannelHandlerContext, byte[])", method.getDeclaringClass().getCanonicalName(), method.getName()));
                            }
                        }else if(method.getParameterTypes().length == 3){
                            if (!ClassUtils.isAssignable(int.class, method.getParameterTypes()[0])
                                    || !ClassUtils.isAssignable(byte[].class, method.getParameterTypes()[1])
                                    || !ClassUtils.isAssignable(ChannelHandlerContext.class, method.getParameterTypes()[2])) {
                                throw new ZeroException(String.format("允许%s#%s(long, byte[], ChannelHandlerContext)", method.getDeclaringClass().getCanonicalName(), method.getName()));
                            }
                        }
                        spiMethodHandlerMap.put(handlerKey, spiMethodHandler);
                        System.out.println(String.format("注册Zero指令%s", handlerKey));
                    },
                    method -> !method.isSynthetic() && AnnotationUtils.findAnnotation(method, SPI.class) != null
            );
        }
        // 扫描过滤器
        ZeroFilter zeroFilter = AnnotationUtils.findAnnotation(bean.getClass(), ZeroFilter.class);
        if (zeroFilter != null) {
            System.out.println(String.format("增加过滤器%s", bean.getClass()));
            this.filters.add(new ZeroFilterValue(zeroFilter.order(), zeroFilter.cmds(), zeroFilter.ignoreCmds(), (Filter) bean));
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public SpiMethodHandler getSpiMethodHandler(int cmd, int version) {
        String handlerKey = buildeHandlerKey(cmd, version);
        SpiMethodHandler handler = spiMethodHandlerMap.get(handlerKey);
        if (handler == null) {
            throw new ZeroException(handlerKey+" 请求指令无效");
        }
        return handler;
    }

    public TreeSet<ZeroFilterValue> getFilters() {
        return filters;
    }

    private String buildeHandlerKey(int cmd, int version) {
        return new StringBuilder().append(cmd).append("#").append(version).toString();
    }
}
