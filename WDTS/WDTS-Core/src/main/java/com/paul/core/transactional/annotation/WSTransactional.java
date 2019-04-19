package com.paul.core.transactional.annotation;

import java.lang.annotation.*;

/**
 * 事务注解，start和end代表事务的开始和结束
 */

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WSTransactional {
    boolean isStart() default false;
    boolean isEnd() default false;
}
