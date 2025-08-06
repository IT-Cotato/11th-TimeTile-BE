package cotato.timetile.domain.post.domain;

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


@Document(indexName = "posts")
@Setting(settingPath = "elasticsearch/post-settings.json")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class PostDocument {

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
    private String title;

    @Field(type = FieldType.Text, analyzer = "korean_english_analyzer")
    private String content;

    public static PostDocument of(Post post) {
        return PostDocument.builder()
                .id(post.getId().toString())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

}
