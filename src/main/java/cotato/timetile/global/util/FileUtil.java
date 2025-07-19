package cotato.timetile.global.util;

import cotato.timetile.global.common.AllowedFileInfo;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static boolean isAllowed(String extension, String mediaType, Long size) {
        return AllowedFileInfo.of(extension)
                .map(info -> info.getMediaType().equals(mediaType) && info.getSize() >= size)
                .orElse(false);
    }

    public static boolean isPresent(MultipartFile multiPartFile) {
        return multiPartFile != null && !multiPartFile.isEmpty();
    }

    public static boolean isAbsent(MultipartFile multiPartFile) {
        return !isPresent(multiPartFile);
    }

}
