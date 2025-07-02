package cotato.timetile.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImageValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Image {
    String message() default "지원하지 않는 이미지 형식";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] allowed() default {"jpg", "jpeg", "png"};
}
