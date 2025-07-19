package cotato.timetile.domain.post.application;

import cotato.timetile.domain.post.api.dto.PostLoadFileUrlDto;
import cotato.timetile.domain.post.api.request.PostLoadFileUrlRequest;
import cotato.timetile.domain.post.api.response.PostLoadFileUrlResponse;
import cotato.timetile.global.common.S3Folder;
import cotato.timetile.global.handler.S3Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostFileService {
    
    private final S3Handler s3Handler;

    public PostLoadFileUrlResponse loadUrl(PostLoadFileUrlRequest request) {
        return PostLoadFileUrlResponse.of(
                request.extensions().stream().map(extension -> {
                            String key = s3Handler.generateKey(extension, S3Folder.POST);
                            String url = s3Handler.generateSignedPutUrl(key);
                            return PostLoadFileUrlDto.of(key, url);
                        }
                ).toList()
        );
    }

}
