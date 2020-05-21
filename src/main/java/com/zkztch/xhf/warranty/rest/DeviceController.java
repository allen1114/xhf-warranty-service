package com.zkztch.xhf.warranty.rest;

import com.zkztch.xhf.warranty.domain.Device;
import com.zkztch.xhf.warranty.service.DeviceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("device")
public class DeviceController {

    private DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("register")
    public Device register(@RequestBody Device device) {
        return deviceService.register(device);
    }
}
