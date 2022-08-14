package dev.dl.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SuppressWarnings("unchecked")
@Configuration
@Slf4j
public class RequestLoggingFilterConfig {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }

    @Bean
    public FilterRegistrationBean<CorsFilterConfig> filterRegistrationBean() {
        FilterRegistrationBean<CorsFilterConfig> registrationBean = new FilterRegistrationBean();
        CorsFilterConfig customURLFilter = new CorsFilterConfig();

        registrationBean.setFilter(customURLFilter);
        registrationBean.setOrder(2); //set precedence
        return registrationBean;
    }

}
