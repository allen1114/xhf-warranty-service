package com.zkztch.xhf.warranty.domain;

import com.zkztch.xhf.warranty.Consts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

public class DeviceTest {

    @Test
    public void getWarrantyPeriodTest() {
        Device device = new Device();
        device.setRegisterTime(new Timestamp(System.currentTimeMillis()));

        Assertions.assertEquals(device.getRegisterTime().getTime() + Consts.WARRANTY_PERIOD, device.getWarrantyPeriod().getTime());
    }
}
