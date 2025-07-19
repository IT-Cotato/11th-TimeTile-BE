package cotato.timetile.global.common;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AllowedFileInfo {
    MP4("mp4", "video/mp4", 50 * 1024 * 1024),
    PNG("png", "image/png", 3 * 1024 * 1024),
    JPG("jpg", "image/jpg", 3 * 1024 * 1024),
    JPEG("jpeg", "image/jpeg", 3 * 1024 * 1024);

    private final String extension;
    private final String mediaType;
    private final long size;

    public static Optional<AllowedFileInfo> of(String extension) {
        return Arrays.stream(values())
                .filter(info -> info.extension.equalsIgnoreCase(extension))
                .findFirst();
    }
}
