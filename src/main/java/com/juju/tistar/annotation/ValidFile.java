package com.juju.tistar.annotation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidFileValidator.class)
public @interface ValidFile {

    String message() default "PNG, JPG, JPEG 파일만 업로드 가능합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}