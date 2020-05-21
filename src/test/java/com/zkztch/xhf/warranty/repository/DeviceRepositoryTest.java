package com.zkztch.xhf.warranty.repository;

import com.zkztch.xhf.warranty.domain.Device;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.config.name=application-h2")
public class DeviceRepositoryTest {

    @Autowired
    public DeviceRepository deviceRepository;


    private String imei = "460000000000000";
    private String sn = "200000000000000";

    @BeforeEach
    public void setup() {
        deviceRepository.deleteAll();
    }

    @Test
    public void createWhenSave() {
        Device device = new Device();
        device.setImei(imei);
        device.setSn(sn);

        Device d = deviceRepository.save(device);

        Assertions.assertNotNull(d.getId());
        Assertions.assertEquals(d.getImei(), device.getImei());
        Assertions.assertEquals(d.getSn(), device.getSn());
        Assertions.assertNotNull(d.getRegisterTime());
    }

    @Test
    public void returnNullWhenNoSave() {
        Assertions.assertNull(deviceRepository.findByImei(imei));
        Assertions.assertNull(deviceRepository.findBySn(sn));
    }

    @Test
    public void foundWhenSaved() {
        Device device = new Device();
        device.setImei(imei);
        device.setSn(sn);
        deviceRepository.save(device);

        Assertions.assertNotNull(deviceRepository.findByImei(imei));
        Assertions.assertNotNull(deviceRepository.findBySn(sn));

    }
}
