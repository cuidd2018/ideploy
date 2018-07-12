package io.ideploy.deployment.admin.configure;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: code4china
 * @description:
 * @date: Created in 11:22 2018/7/12
 */
@Configuration
public class WebStaticConfigure implements WebMvcConfigurer{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/").setCacheControl(
                CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
    }
}
