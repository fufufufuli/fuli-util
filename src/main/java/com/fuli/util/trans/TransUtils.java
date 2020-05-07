package com.fuli.util.trans;

import com.fuli.util.Commons;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.Instant;
import java.util.Objects;

/**
 * 转换参数
 *
 * @author fuli
 */
public class TransUtils {

    private static final String[] PATTERNS = {
            "yyyy-MM-dd HH:mm:ss",
            "yy-MM-dd HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyyMMdd"
    };

    public static BigDecimal transBigDecimal(Object obj) {
        Preconditions.checkArgument(Objects.nonNull(obj));
        if (obj instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        } else if (obj instanceof String str && Commons.isNotEmpty(String.valueOf(obj))) {
            return new BigDecimal(str);
        }
        throw new TransException(obj.getClass().getSimpleName());
    }

    public static Integer transInteger(Object obj) {
        Preconditions.checkArgument(Objects.nonNull(obj));
        if (obj instanceof Integer integer) {
            return integer;
        } else if (obj instanceof String str && Commons.isNotEmpty(str)) {
            return Integer.valueOf(str);
        }
        throw new TransException(obj.getClass().getSimpleName());
    }

    public static Instant transInstant(String value) {
        Preconditions.checkArgument(Commons.isEmpty(value));
        try {
            return DateUtils.parseDate(value, PATTERNS).toInstant();
        } catch (ParseException e) {
            throw new TransException("data format error:", e);
        }
    }
}
