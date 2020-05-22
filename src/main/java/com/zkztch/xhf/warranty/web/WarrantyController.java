package com.zkztch.xhf.warranty.web;

import com.zkztch.xhf.warranty.domain.Device;
import com.zkztch.xhf.warranty.service.DeviceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/warranty")
@Controller
public class WarrantyController {

    private DeviceService deviceService;

    public WarrantyController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public String period(@RequestParam(value = "p", required = false) String p, Model model) {

        if (StringUtils.isBlank(p)) {
            return "warranty_input";
        }

        Device device = deviceService.findByImei(p);
        device = device == null ? deviceService.findBySn(p) : device;

        if (device == null) {
            return "warranty_notfound";
        } else {
            model.addAttribute("device", device);
            return "warranty_period";
        }
    }
}
