package cotato.timetile.global.converter;

import cotato.timetile.domain.event.domain.ChangeType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ChangeTypeConverter implements Converter<String, ChangeType> {

    @Override
    public ChangeType convert(String source) {
        return ChangeType.valueOf(source.toUpperCase());
    }

}
