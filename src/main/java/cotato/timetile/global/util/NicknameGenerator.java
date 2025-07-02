package cotato.timetile.global.util;

import java.util.List;
import java.util.Random;

public class NicknameGenerator {

    private static final List<String> ADJECTIVES = List.of(
            "귀여운", "용감한", "조용한", "신나는", "우아한", "활발한", "명랑한", "상냥한",
            "똑똑한", "즐거운", "배고픈", "행복한", "느긋한", "재빠른", "열정적인", "친절한"
    );

    private static final List<String> NOUNS = List.of(
            "호랑이", "고양이", "강아지", "토끼", "여우", "곰", "다람쥐", "판다",
            "사자", "기린", "늑대", "햄스터", "참새", "하마", "너구리", "알파카"
    );

    private static final Random RANDOM = new Random();
    private static final int DIGIT = 5;
    private static final int BOUND = (int) Math.pow(10, DIGIT);

    public static String generateNickname() {
        String adjective = ADJECTIVES.get(RANDOM.nextInt(ADJECTIVES.size()));
        String noun = NOUNS.get(RANDOM.nextInt(NOUNS.size()));
        int number = RANDOM.nextInt(BOUND);
        return String.format("%s%s%0" + DIGIT + "d", adjective, noun, number);
    }

}
