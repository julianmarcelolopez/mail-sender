package com.jpdevs.mailsender.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
				.addResourceHandler("/swagger-ui/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/")
				.setCachePeriod(0)
				.resourceChain(false);
	}

	@Bean
	public FilterRegistrationBean<Filter> responseHeaderFilter() {
		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request,
											HttpServletResponse response,
											FilterChain filterChain)
					throws ServletException, IOException {

				if (request.getRequestURI().contains("/swagger-ui/") ||
						request.getRequestURI().contains("/v3/api-docs")) {
					response.setHeader("Content-Type", "application/javascript; charset=UTF-8");
					response.setHeader("Cache-Control", "no-store");
				}
				filterChain.doFilter(request, response);
			}
		});
		registrationBean.addUrlPatterns("/swagger-ui/*", "/v3/api-docs/*");
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return registrationBean;
	}
}