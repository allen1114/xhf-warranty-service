package com.zkztch.xhf.warranty.rest;

import com.zkztch.xhf.warranty.domain.Device;
import com.zkztch.xhf.warranty.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("device")
@Api(tags = {"设备服务"})
public class DeviceController {
    private DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @ApiOperation("设备注册")
    @PostMapping()
    public Device register(@RequestBody Device device) {
        return deviceService.register(device);
    }

    @ApiOperation("获取设备信息")
    @GetMapping("/{p}")
    public Device getDevice(@ApiParam("imei 或 sn") @PathVariable("p") String p) {
        Device device = deviceService.findByImei(p);
        device = device == null ? deviceService.findBySn(p) : device;
        return device == null ? new Device() : device;
    }
}
