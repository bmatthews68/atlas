package com.btmatthews.atlas.validation.esapi;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InputFieldValidator.class)
@Documented
public @interface InputField {

    String message() default "com.btmatthews.atlas.validation.esapi.InputField.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String context() default "";

    /**
     * The key used to retrieve the regular expression for validation of the input field from the ESAPI configuration.
     */
    String pattern();

    /**
     * The maximum length of the input field.
     */
    int maximumLength() default Integer.MAX_VALUE;

    /**
     * /**
     * Indicates whether the input field can be {@code null} or not.
     */
    boolean allowNull() default true;
}
