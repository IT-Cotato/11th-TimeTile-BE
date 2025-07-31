package cotato.timetile.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MediaValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Media {
    String message() default "지원하지 않는 미디어 형식";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] allowed() default {"jpg", "jpeg", "png", "mp4"};
}
