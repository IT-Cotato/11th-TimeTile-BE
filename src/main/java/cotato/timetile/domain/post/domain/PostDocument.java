package cotato.timetile.domain.post.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "posts")
@AllArgsConstructor
public class PostDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String title;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String content;

    public static PostDocument of(Post post) {
        return new PostDocument(
                post.getId().toString(),
                post.getTitle(),
                post.getContent()
        );
    }

}
