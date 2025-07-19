package cotato.timetile.domain.post.api.response;

import cotato.timetile.domain.post.api.dto.PostLoadFileUrlDto;
import java.util.List;

public record PostLoadFileUrlResponse(
        List<PostLoadFileUrlDto> uploadInfo
) {
    public static PostLoadFileUrlResponse of(List<PostLoadFileUrlDto> uploadInfo) {
        return new PostLoadFileUrlResponse(uploadInfo);
    }
}
