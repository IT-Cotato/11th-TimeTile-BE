package cotato.timetile.domain.post.api.request;

import cotato.timetile.global.common.Visibility;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record PostUpdateRequest(
        @Size(max = 50, min = 1) String title,
        @Size(max = 500, min = 1) String content,
        @NotNull Visibility visibility,
        List<String> mediaKeys,
        Integer mainImageIndex
) {
}
