package dev.dl.common.helper;

import dev.dl.common.exception.DLException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class ValidateHelper {

    public static boolean validate(String regex, String strToCheck) throws DLException {
        if (ObjectHelper.isNullOrEmpty(regex) || ObjectHelper.isNullOrEmpty(strToCheck)) {
            throw DLException.newBuilder()
                    .message("regex and strToCheck can not be null or empty")
                    .timestamp(LocalDateTime.now())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(strToCheck);
        return (match.find() && match.group().equals(strToCheck));
    }

}
