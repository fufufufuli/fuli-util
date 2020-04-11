package com.fuli.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.Instant;

/**
 * 转换参数
 *
 * @author fuli
 */
public class TransferUtils {

    private static final String[] PATTERNS = {
            "yyyy-MM-dd HH:mm:ss",
            "yy-MM-dd HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyyMMdd"
    };

    public static BigDecimal transBigDecimal(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Integer integer) {
            return new BigDecimal(integer);
        } else if (obj instanceof String str && StringUtils.isNotBlank(String.valueOf(obj))) {
            return new BigDecimal(str);
        }
        throw new TransferException(obj.getClass().getSimpleName());
    }

    public static Integer transInteger(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Integer integer) {
            return integer;
        } else if (obj instanceof String str && StringUtils.isNotBlank(str)) {
            return Integer.valueOf(str);
        }
        throw new TransferException(obj.getClass().getSimpleName());
    }

    public static Instant transInstant(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return DateUtils.parseDate(value, PATTERNS).toInstant();
        } catch (ParseException e) {
            throw new TransferException("data format error:", e);
        }
    }
}
