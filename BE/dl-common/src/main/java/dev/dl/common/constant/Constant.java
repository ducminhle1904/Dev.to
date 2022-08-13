package dev.dl.common.constant;

import org.apache.logging.log4j.util.Strings;

import java.util.List;

public final class Constant {

    public static final String EMPTY_STRING = Strings.EMPTY;

    public static final String DEFAULT_TIMEZONE = "Asia/Ho_Chi_Minh";
    public static final String SPACE = " ";

    public static final String SUCCESS_MESSAGE = "SUCCESS";
    public static final String FAILED_MESSAGE = "FAILED";

    public static final String SUCCESS = "success";

    public static final String WARNING = "warning";

    public static final String ERROR = "error";
    public static final float MEGABYTE_TO_BYTE = 1_000_000.0f;

    public static final Integer RESPONSE_OK = 200;

    public static final int MAX_IMAGE_PER_PRODUCT = 5;
    public static final int DAY_IN_A_YEAR = 365;
    public static final double A_BYTE_TO_MEGABYTE = 0.000001d;

    public static final List<String> WHILE_LIST_API = List.of(
            "/swagger-ui/",
            "/v2/api-docs",
            "/swagger-ui.html",
            "/swagger-ui/springfox.css",
            "/swagger-ui/swagger-ui-standalone-preset.js",
            "/swagger-ui/springfox.js",
            "/swagger-ui/favicon-32x32.png",
            "/swagger-ui/favicon-16x16.png",
            "/swagger-ui/swagger-ui.css",
            "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-resources/configuration/ui",
            "/swagger-resources/configuration/security",
            "/swagger-resources",
            "/",
            "/api/v1/log",
            "/api/v1/log/all",
            "/api/v1/log/get-with-name"
    );

    public static final String GOOGLE_OAUTH_PROVIDER = "Google";

    public static final Integer MAX_PRODUCT_CART_QUANTITY = 100;

    public static final String HYPHEN = "-";

    public static final String UNDERSCORE = "_";

    public static final String PRODUCT_IMAGE_CATEGORY = "product";

    public static final String USER_IMAGE_CATEGORY = "user";

    public static final String SLASH = "/";

    public static final String BACKSLASH = "\\";

    public static final Integer MAX_PRODUCT_RECENTLY_VIEWED = 8;

    public static final String ADMIN_ROLE = "ADMIN";

    public static final String MODERATOR_ROLE = "MODERATOR";

    public static final String USER_ROLE = "USER";
}
