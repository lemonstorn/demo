package com.weaver.core.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字符串工具类
 *
 * @author zh 2023/8/4 22:37
 */
public class StringUtils {
    @Contract("null -> !null")
    @SuppressWarnings({"unchecked"})
    public static String toString(Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).stripTrailingZeros().toPlainString();
        } else if (value instanceof String || value instanceof Number) {
            return value.toString();
        } else if (value.getClass().isArray()) {
            return join(Arrays.stream((Object[]) value).map(StringUtils::toString).collect(Collectors.toList()));
        } else if (value instanceof List) {
            return join(((List<Object>) value).stream().map(StringUtils::toString).collect(Collectors.toList()));
        } else {
            return value.toString();
        }
    }

    @NotNull
    @Contract("_ -> new")
    public static String join(List<String> list) {
        return String.join(",", list);
    }

    @Contract("null -> true")
    public static boolean isBlank(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isBlank(cs);
    }

    public static boolean isBlank(Object outPath) {
        if (outPath == null) {
            return true;
        }
        return isBlank(toString(outPath));
    }

    @Contract("null -> true")
    public static boolean isAllBlank(final CharSequence... cs) {
        return org.apache.commons.lang3.StringUtils.isAllBlank(cs);
    }

    @Contract("null -> false")
    public static boolean isNotBlank(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(cs);
    }

    @Contract("null -> true")
    public static boolean isNoneBlank(final CharSequence... cs) {
        return org.apache.commons.lang3.StringUtils.isNoneBlank(cs);
    }

    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        return org.apache.commons.lang3.StringUtils.equals(cs1, cs2);
    }

    @Contract("!null -> !null; null -> null")
    public static String uncapitalize(final String str) {
        return org.apache.commons.lang3.StringUtils.uncapitalize(str);
    }

    /**
     * 转为16进制
     * @param input 输入字符串
     * @return  16进制字符串
     */
    public static String hex(String input) {
        char[] chars = input.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char c : chars) {
            hex.append(Integer.toHexString(c));
        }
        return hex.toString();
    }

    /**
     * 16 进制串转字节数组
     *
     * @param hex 16进制字符串
     * @return byte数组
     */
    public static byte[] hexToBytes(String hex) {
        int length = hex.length();
        byte[] result;
        if (length % 2 == 1) {
            length++;
            result = new byte[(length / 2)];
            hex = "0" + hex;
        } else {
            result = new byte[(length / 2)];
        }
        int j = 0;
        for (int i = 0; i < length; i += 2) {
            result[j] = (byte) Integer.parseInt((hex.substring(i, i + 2)), 16);
            j++;
        }
        return result;
    }
}
