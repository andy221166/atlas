package org.atlas.platform.api.server.rest.config;

import org.atlas.platform.api.server.rest.context.UserContextFilter;
import org.atlas.platform.api.server.rest.logging.RequestLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  private static final String API_PATH_SERVLET_PATTERN = "/api/*";

  @Bean
  public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilterRegistration() {
    FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new RequestLoggingFilter());
    registrationBean.addUrlPatterns(API_PATH_SERVLET_PATTERN);
    registrationBean.setOrder(0);
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean<UserContextFilter> currentUserFilterFilterRegistration() {
    FilterRegistrationBean<UserContextFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new UserContextFilter());
    registrationBean.addUrlPatterns(API_PATH_SERVLET_PATTERN);
    registrationBean.setOrder(1);
    return registrationBean;
  }
}
