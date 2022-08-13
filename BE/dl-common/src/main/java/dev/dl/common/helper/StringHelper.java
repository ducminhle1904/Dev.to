package dev.dl.common.helper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("ALL")
public class StringHelper {

    public static String stripAccents(String s) {
        s = StringUtils.stripAccents(s);
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s.replace("đ", "d").replace("Đ", "D");
    }

    public static String textToUnicode(String str) {
        return StringEscapeUtils.escapeJava(str);
    }

    public static String unicodeToText(String str) {
        return StringEscapeUtils.unescapeJava(str);
    }

    /**
     * Convert String to LocalDateTime
     *
     * @param date example: 2016-03-04 11:30
     * @return
     */
    public LocalDateTime convertStringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    /*
     * public static boolean isNullOrEmpty(String str) {
     *     return Optional.ofNullable(str).isEmpty() || str.trim().isEmpty();
     * }
     */

    public static String unicodeStringToAscii(String unicodeString) {
        return Normalizer.normalize(unicodeString, Normalizer.Form.NFD);
    }

}
