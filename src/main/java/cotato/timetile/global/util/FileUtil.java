package cotato.timetile.global.util;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static boolean isPresent(MultipartFile multiPartFile) {
        return multiPartFile != null && !multiPartFile.isEmpty();
    }

    public static boolean isAbsent(MultipartFile multiPartFile) {
        return !isPresent(multiPartFile);
    }

}
