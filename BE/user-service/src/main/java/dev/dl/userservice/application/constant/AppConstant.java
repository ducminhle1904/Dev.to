package dev.dl.userservice.application.constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppConstant {

    public static final Map<String, List<String>> AUTHENTICATE_API = new HashMap<>();

    static {
        AUTHENTICATE_API.put("/api/user/validate", List.of("ADMIN"));
    }

}
