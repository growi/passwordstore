package dev.growi.passwordstore.server.core.base.mapping.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MappedValue {

    public String mappedClass();

    public String mappedValue();

    public String transformer() default "";

    public String method() default "";
}
