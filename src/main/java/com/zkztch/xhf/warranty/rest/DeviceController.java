package com.zkztch.xhf.warranty.rest;

import com.zkztch.xhf.warranty.domain.Device;
import com.zkztch.xhf.warranty.service.DeviceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("device")
public class DeviceController {
    private DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @ApiOperation("设备注册")
    @PostMapping("register")
    public Device register(@RequestParam("imei") String imei, @RequestParam("sn") String sn) {
        Device device = new Device();
        device.setImei(imei);
        device.setSn(sn);
        return deviceService.register(device);
    }

    @ApiOperation("获取设备")
    @GetMapping("/{p}")
    public Device getDevice(@PathVariable("p") String p) {
        Device device = deviceService.findByImei(p);
        device = device == null ? deviceService.findBySn(p) : device;
        return device == null ? new Device() : device;
    }
}
