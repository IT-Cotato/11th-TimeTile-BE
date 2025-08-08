package cotato.timetile.global.handler;

import cotato.timetile.global.common.S3Folder;
import cotato.timetile.global.exception.UnprocessableEntityException;
import cotato.timetile.global.properties.S3Properties;
import cotato.timetile.global.util.FileUtil;
import io.awspring.cloud.s3.S3Operations;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

@Component
@RequiredArgsConstructor
public class S3Handler {

    private static final Duration GET_VALID_DURATION = Duration.ofHours(1);
    private static final Duration PUT_VALID_DURATION = Duration.ofMinutes(10);
    private final S3Operations s3Operations;
    private final S3Properties s3Properties;
    private final S3Client s3Client;

    public void deleteFile(String key) {
        if (key == null) {
            return;
        }
        s3Operations.deleteObject(s3Properties.bucket(), key);
    }

    public String generateSignedGetUrl(String key) {
        return s3Operations.createSignedGetURL(s3Properties.bucket(), key, GET_VALID_DURATION).toString();
    }

    public String generateSignedPutUrl(String key) {

        return s3Operations.createSignedPutURL(s3Properties.bucket(), key, PUT_VALID_DURATION).toString();
    }

    public String generateKey(String extension, S3Folder s3Folder) {
        return String.format(
                "%s/%s.%s",
                s3Folder.toString().toLowerCase(),
                UUID.randomUUID(),
                extension
        );
    }

    public String getSimpleLogoUrlIfNull(String key) {
        return s3Operations.createSignedGetURL(
                        s3Properties.bucket(),
                        Objects.requireNonNullElse(key, s3Properties.simpleLogoKey()),
                        GET_VALID_DURATION)
                .toString();
    }

    public void deleteNotAllowedFile(String mediaKey) {
        if (mediaKey == null) {
            return;
        }
        if (!validateMetadata(mediaKey)) {
            deleteFile(mediaKey);
            throw UnprocessableEntityException.invalid();
        }
    }

    public void deleteNotAllowedFiles(List<String> mediaKeys) {
        List<String> invalidMediaKeys = mediaKeys.stream()
                .filter(key -> !validateMetadata(key))
                .toList();
        if (!invalidMediaKeys.isEmpty()) {
            invalidMediaKeys.forEach(this::deleteFile);
            throw UnprocessableEntityException.invalid();
        }
    }

    private boolean validateMetadata(String key) {
        HeadObjectResponse headObjectResponse = loadMetadata(key);
        String extension = key.substring(key.lastIndexOf('.') + 1);
        return FileUtil.isAllowed(extension, headObjectResponse.contentType(), headObjectResponse.contentLength());
    }

    private HeadObjectResponse loadMetadata(String key) {
        return s3Client.headObject(
                HeadObjectRequest.builder()
                        .bucket(s3Properties.bucket())
                        .key(key)
                        .build()
        );
    }

}
