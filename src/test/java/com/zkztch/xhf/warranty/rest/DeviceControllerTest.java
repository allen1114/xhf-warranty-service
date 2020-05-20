package com.zkztch.xhf.warranty.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeviceControllerTest {

    @Test
    public void getEnvMappingTest() throws Exception {
        DeviceController controller = new DeviceController();
        Assertions.assertEquals(controller.hello(), "hello");
    }
}
