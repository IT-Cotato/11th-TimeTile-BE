package cotato.timetile.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "users")
@Setting(settingPath = "elasticsearch/user-settings.json")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class UserDocument {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "korean_english_analyzer"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword),
                    @InnerField(suffix = "edge", type = FieldType.Text, analyzer = "edge_ngram_analyzer")
            }
    )
    private String nickname;

    public static UserDocument of(User user) {
        return UserDocument.builder()
                .id(user.getId().toString())
                .nickname(user.getNickname())
                .build();
    }

}
