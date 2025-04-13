package org.atlas.infrastructure.api.server.rest.core.config;

import org.atlas.infrastructure.api.server.rest.core.logging.RequestLoggingFilter;
import org.atlas.infrastructure.api.server.rest.core.security.UserContextFilter;
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
