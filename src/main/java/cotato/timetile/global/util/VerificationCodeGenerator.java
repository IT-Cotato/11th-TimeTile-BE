package cotato.timetile.global.util;

import java.util.Random;

public class VerificationCodeGenerator {

    private static final Random random = new Random();
    private static final int DIGIT = 6;
    private static final int BOUND = (int) Math.pow(10, DIGIT);

    public static String generateVerificationCode() {
        int verificationCode = random.nextInt(BOUND);
        return String.format("%0" + DIGIT + "d", verificationCode);
    }

}
