package cotato.timetile.global.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cotato.timetile.global.properties.ElasticsearchProperties;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ElasticsearchConfig {

    private final ElasticsearchProperties elasticsearchProperties;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper(objectMapper);

        RestClientTransport restClientTransport = new RestClientTransport(
                RestClient.builder(new HttpHost(
                                        elasticsearchProperties.hostname(),
                                        Integer.parseInt(elasticsearchProperties.port())
                                )
                        )
                        .build(),
                jacksonJsonpMapper
        );

        return new ElasticsearchClient(restClientTransport);
    }

}
