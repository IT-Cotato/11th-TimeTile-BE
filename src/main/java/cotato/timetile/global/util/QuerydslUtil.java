package cotato.timetile.global.util;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

public class QuerydslUtil {

    public static <T extends Comparable<?>> BooleanExpression safeEq(ComparableExpressionBase<T> path, T value) {
        return value != null ? path.eq(value) : null;
    }

}
