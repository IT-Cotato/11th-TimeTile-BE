package cotato.timetile.domain.event.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "events")
@Setting(settingPath = "elasticsearch/event-settings.json")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class EventDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String groupId;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "korean_english_analyzer"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword),
                    @InnerField(suffix = "edge", type = FieldType.Text, analyzer = "edge_ngram_analyzer")
            }
    )
    private String name;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "korean_english_analyzer"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword),
                    @InnerField(suffix = "edge", type = FieldType.Text, analyzer = "edge_ngram_analyzer")
            }
    )
    private String artistName;

    @Field(type = FieldType.Date, format = DateFormat.strict_date)
    private LocalDate startedAt;

    public static EventDocument of(Event event) {
        return EventDocument.builder()
                .id(event.getId().toString())
                .groupId(event.getGroupId())
                .name(event.getName())
                .artistName(event.getArtist().getName())
                .startedAt(event.getStartedAt())
                .build();
    }

}
