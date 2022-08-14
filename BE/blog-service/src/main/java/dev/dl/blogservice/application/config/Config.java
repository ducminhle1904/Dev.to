package dev.dl.blogservice.application.config;

import dev.dl.common.config.CorsFilterConfig;
import dev.dl.common.config.RequestLoggingFilterConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Configuration
    public static class CorsConfig extends CorsFilterConfig {

    }
    @Configuration
    public static class RequestLoggingConfig extends RequestLoggingFilterConfig {

    }

}
