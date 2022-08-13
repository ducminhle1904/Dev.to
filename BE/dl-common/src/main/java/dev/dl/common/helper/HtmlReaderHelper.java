package dev.dl.common.helper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("ALL")
public class HtmlReaderHelper {

    public static String readHtmlFile(Class<?> cls, String path) throws Exception {
        InputStream in = cls.getResourceAsStream(path);
        String content;
        try {
            content = IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
            in.close();
        }
        return content;
    }

}
