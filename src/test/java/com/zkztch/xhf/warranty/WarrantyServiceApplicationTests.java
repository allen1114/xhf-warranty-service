package com.zkztch.xhf.warranty;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.config.name=application-h2")
class WarrantyServiceApplicationTests {

    @Test
    void contextLoads() throws InterruptedException {

        System.getProperties().setProperty("spring.config.name", "application-h2");
        SpringApplication.run(WarrantyServiceApplication.class);

        while (true){
            Thread.sleep(5000L);
        }
    }

}
