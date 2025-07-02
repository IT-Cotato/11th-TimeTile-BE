package cotato.timetile.global.properties;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = "cotato.timetile.auth.mail")
public class MailPropertiesConfiguration {
}
