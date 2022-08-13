package dev.dl.common.helper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

@SuppressWarnings("ALL")
public class ExceptionStackTraceHelper {

    public static String getStackTrace(Throwable throwable) {
        try {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            throwable.printStackTrace(printWriter);
            return result.toString();
        } catch(Exception ex) {
            return "Null pointer Exception";
        }
    }

}
