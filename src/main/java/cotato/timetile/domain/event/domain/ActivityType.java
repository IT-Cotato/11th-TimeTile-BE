package cotato.timetile.domain.event.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import cotato.timetile.global.exception.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityType {
    CONCERT_OR_FAN_MEETING("콘서트/팬미팅"),
    FESTIVAL("페스티벌/축제"),
    AWARDS_CEREMONY("시상식"),
    ALBUM_RELEASE("앨범발매"),
    MUSIC_SHOW("음악방송"),
    SHOWCASE("쇼케이스"),
    MINI_FAN_MEETING("미니팬미팅"),
    FAN_SIGNING("팬사인회/기타"),
    YOUTUBE("유튜브"),
    VARIETY_SHOW("TV예능"),
    DRAMA("드라마"),
    RADIO("라디오"),
    MOVIE("영화"),
    PHOTOSHOOT_OR_INTERVIEW("화보/인터뷰"),
    COMMERCIAL_OR_MODEL("광고/모델"),
    COLLABORATION("/콜라보"),
    LIVE("라이브"),
    OTHER("기타");

    @JsonValue
    private final String value;

    @JsonCreator
    public static ActivityType of(String value) {
        for (ActivityType activityType : ActivityType.values()) {
            if (activityType.value.equalsIgnoreCase(value)) {
                return activityType;
            }
        }
        throw BadRequestException.wrong();
    }
}
