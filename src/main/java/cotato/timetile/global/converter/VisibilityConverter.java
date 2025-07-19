package cotato.timetile.global.converter;

import cotato.timetile.global.common.Visibility;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VisibilityConverter implements Converter<String, Visibility> {

    @Override
    public Visibility convert(String source) {
        return Visibility.valueOf(source.toUpperCase());
    }
}
