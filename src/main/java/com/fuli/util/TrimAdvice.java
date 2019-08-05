/*
package com.fuli.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

*/
/**
 * @author fuli
 *//*

@Aspect
@Component
public class TrimAdvice {
    @Before("execution(* cn.magfin.gears..*.*(..))")
    public void trim(JoinPoint jp) {
        Object[] args = jp.getArgs();
        for (Object arg : args) {
            this.trimBean(arg);
        }
    }

    private void trimBean(Object bean) {
        if (bean == null) {
            return;
        }
        boolean flag;
        try {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Trim.class)) {
                    flag = field.isAccessible();
                    field.setAccessible(true);

                    Object value = field.get(bean);
                    if (Objects.nonNull(value)) {
                        field.set(bean, String.valueOf(value).trim());
                    }
                    field.setAccessible(flag);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
*/
