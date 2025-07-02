package cotato.timetile.global.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptUtil {

    private static final BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

    public static String encrypt(String rawPassword) {
        return bcryptEncoder.encode(rawPassword);
    }

    public static boolean verify(String rawPassword, String encodedPassword) {
        return bcryptEncoder.matches(rawPassword, encodedPassword);
    }

}
