package com.zkztch.xhf.warranty;

import com.zkztch.xhf.warranty.repository.DeviceRepository;
import com.zkztch.xhf.warranty.service.DeviceService;
import com.zkztch.xhf.warranty.service.impl.DeviceServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class WarrantyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarrantyServiceApplication.class, args);
    }


    @Bean
    public DeviceService deviceService(DeviceRepository deviceRepository) {
        return new DeviceServiceImpl(deviceRepository);
    }

    @Configuration
    public static class MvcConfig extends WebMvcConfigurationSupport {
        @Override
        protected void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/static/**").addResourceLocations(
                    "classpath:/static/");
            registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                    "classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/");
            super.addResourceHandlers(registry);
        }
    }
}
