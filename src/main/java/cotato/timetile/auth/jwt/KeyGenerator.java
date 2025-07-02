package cotato.timetile.auth.jwt;

import io.jsonwebtoken.io.Decoders;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KeyGenerator {

    private static final String HMAC_SHA256 = "HmacSHA256";

    public static SecretKey getKeyFromString(String keyString) {
        byte[] keyBytes = Decoders.BASE64.decode(keyString);
        return new SecretKeySpec(keyBytes, HMAC_SHA256);
    }

}