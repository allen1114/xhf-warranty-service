package com.zkztch.xhf.warranty.service.impl;

import com.zkztch.xhf.warranty.domain.Device;
import com.zkztch.xhf.warranty.repository.DeviceRepository;
import com.zkztch.xhf.warranty.service.DeviceService;
import org.springframework.transaction.annotation.Transactional;

public class DeviceServiceImpl implements DeviceService {

    private DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    @Transactional
    public Device register(Device d) {
        Device device = deviceRepository.findByImei(d.getImei());
        if (device != null) {
            return device;
        }

        device = deviceRepository.findBySn(d.getSn());
        if (device != null) {
            return device;
        }

        return deviceRepository.save(d);
    }

    @Override
    public Device findByImei(String imei) {
        return deviceRepository.findByImei(imei);
    }

    @Override
    public Device findBySn(String sn) {
        return deviceRepository.findBySn(sn);
    }
}
