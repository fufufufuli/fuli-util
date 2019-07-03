package com.fuli.pudding.util;

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
        } else if (obj instanceof Integer) {
            return new BigDecimal((Integer) obj);
        } else if (obj instanceof String && StringUtils.isNotBlank(String.valueOf(obj))) {
            return new BigDecimal((String) obj);
        }
        throw new TransferException(obj.getClass().getSimpleName());
    }

    public static Integer transInteger(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Integer) {
            return (Integer) obj;
        } else if (obj instanceof String) {
            if (StringUtils.isNotBlank((String) obj)) {
                return Integer.valueOf((String) obj);
            }
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
            throw new TransferException("时间格式转换错误", e);
        }
    }
}
