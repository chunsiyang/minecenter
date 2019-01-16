package com.minecenter.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * 自定义事务注解封装Spring Transactional 原有属性
 * 添加 enableRedisTransactional 属性标记是否开启redis事务支持
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@org.springframework.transaction.annotation.Transactional
public @interface CustomTransactional {

    /**
     * 是否开启redis事务支持
     */
    boolean enableRedisTransactional() default false;

    @AliasFor(attribute = "value", annotation = Transactional.class)
    String value() default "";

    @AliasFor(attribute = "transactionManager", annotation = Transactional.class)
    String transactionManager() default "";

    @AliasFor(attribute = "propagation", annotation = Transactional.class)
    Propagation propagation() default Propagation.REQUIRED;

    @AliasFor(attribute = "isolation", annotation = Transactional.class)
    Isolation isolation() default Isolation.DEFAULT;

    @AliasFor(attribute = "timeout", annotation = Transactional.class)
    int timeout() default -1;

    @AliasFor(attribute = "readOnly", annotation = Transactional.class)
    boolean readOnly() default false;

    @AliasFor(attribute = "rollbackFor", annotation = Transactional.class)
    Class<? extends Throwable>[] rollbackFor() default {};

    @AliasFor(attribute = "rollbackForClassName", annotation = Transactional.class)
    String[] rollbackForClassName() default {};

    @AliasFor(attribute = "noRollbackFor", annotation = Transactional.class)
    Class<? extends Throwable>[] noRollbackFor() default {};

    @AliasFor(attribute = "noRollbackForClassName", annotation = Transactional.class)
    String[] noRollbackForClassName() default {};
}
