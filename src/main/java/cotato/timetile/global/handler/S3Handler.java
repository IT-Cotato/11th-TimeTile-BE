package cotato.timetile.global.handler;

import cotato.timetile.global.common.S3Folder;
import cotato.timetile.global.properties.S3Properties;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class S3Handler {

    private final S3Operations s3Operations;
    private final S3Properties s3Properties;

    public void uploadFile(MultipartFile multipartFile, String key) {
        try {
            s3Operations.upload(s3Properties.bucket(), key, multipartFile.getInputStream(),
                    ObjectMetadata.builder().contentType(multipartFile.getContentType()).build());
        } catch (IOException e) {
            throw cotato.timetile.global.exception.IOException.upload();
        }
    }

    public void deleteFile(String key) {
        s3Operations.deleteObject(s3Properties.bucket(), key);
    }

    public String getPresignedUrl(String key, Duration duration) {
        return s3Operations.createSignedGetURL(s3Properties.bucket(), key, duration).getPath();
    }

    public String generateKey(MultipartFile multipartFile, S3Folder s3Folder) {
        return String.format(
                "%s/%s.%s",
                s3Folder.toString().toLowerCase(),
                UUID.randomUUID(),
                FilenameUtils.getExtension(multipartFile.getOriginalFilename())
        );
    }

}
