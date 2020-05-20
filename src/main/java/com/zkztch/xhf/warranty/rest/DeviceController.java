package com.zkztch.xhf.warranty.rest;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceController {

    @RequestMapping
    public String hello() {
        return "hello";
    }
}
