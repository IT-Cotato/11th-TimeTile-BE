package cotato.timetile.domain.user.application;

import cotato.timetile.domain.user.api.request.UserLoadFileUrlRequest;
import cotato.timetile.domain.user.api.response.UserLoadFileUrlResponse;
import cotato.timetile.global.common.S3Folder;
import cotato.timetile.global.handler.S3Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFileService {

    private final S3Handler s3Handler;

    public UserLoadFileUrlResponse loadUrl(UserLoadFileUrlRequest request) {
        String key = s3Handler.generateKey(request.extension(), S3Folder.USER);
        String url = s3Handler.generateSignedPutUrl(key);
        return UserLoadFileUrlResponse.of(key, url);
    }
    
}
