package cotato.timetile.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class MediaValidator implements ConstraintValidator<Media, List<String>> {

    private List<String> allowedExtensions;

    @Override
    public void initialize(Media constraintAnnotation) {
        allowedExtensions = Arrays.stream(constraintAnnotation.allowed())
                .map(String::toLowerCase)
                .toList();
    }

    @Override
    public boolean isValid(List<String> extensions, ConstraintValidatorContext constraintValidatorContext) {
        return new HashSet<>(allowedExtensions).containsAll(extensions);
    }

}
