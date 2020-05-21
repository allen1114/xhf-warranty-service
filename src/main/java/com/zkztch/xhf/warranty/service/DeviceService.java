package com.zkztch.xhf.warranty.service;

import com.zkztch.xhf.warranty.domain.Device;

public interface DeviceService {

    Device register(Device device);

    Device findByImei(String imei);

    Device findBySn(String sn);
}
