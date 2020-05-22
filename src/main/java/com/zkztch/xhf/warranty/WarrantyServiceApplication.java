package com.zkztch.xhf.warranty;

import com.zkztch.xhf.warranty.repository.DeviceRepository;
import com.zkztch.xhf.warranty.service.DeviceService;
import com.zkztch.xhf.warranty.service.impl.DeviceServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
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
}
