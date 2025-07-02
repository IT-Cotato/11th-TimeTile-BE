package cotato.timetile.annotation;

import cotato.timetile.global.util.FileUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {
    private List<String> allowedExtensions;
    private List<String> allowedMediaTypes;

    @Override
    public void initialize(Image constraintAnnotation) {
        allowedExtensions = Arrays.stream(constraintAnnotation.allowed())
                .map(String::toLowerCase)
                .toList();
        allowedMediaTypes = Arrays.stream(constraintAnnotation.allowed())
                .map(ext -> "image/" + ext.toLowerCase())
                .toList();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (FileUtil.isAbsent(multipartFile)) {
            return true;
        }
        return Boolean.logicalAnd(isSupportedImageExtension(multipartFile), isSupportedImageType(multipartFile));
    }

    private boolean isSupportedImageExtension(MultipartFile multipartFile) {
        return allowedExtensions.contains(FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
    }

    private boolean isSupportedImageType(MultipartFile multipartFile) {
        return allowedMediaTypes.contains(multipartFile.getContentType());
    }
}
