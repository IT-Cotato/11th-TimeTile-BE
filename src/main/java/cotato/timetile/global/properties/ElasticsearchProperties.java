package cotato.timetile.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.elasticsearch")
public record ElasticsearchProperties(
        String hostname,
        String port
) {
}
