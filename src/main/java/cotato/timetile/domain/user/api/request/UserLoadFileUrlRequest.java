package cotato.timetile.domain.user.api.request;

import cotato.timetile.annotation.Image;

public record UserLoadFileUrlRequest(
        @Image String extension
) {
}
