package com.zkztch.xhf.warranty.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkztch.xhf.warranty.domain.Device;
import com.zkztch.xhf.warranty.service.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeviceControllerTest {

    private ObjectMapper objectMapper;
    private DeviceService deviceService;
    private DeviceController controller;
    private MockHttpServletRequestBuilder request;
    private MockMvc mvc;
    private String imei = "460000000000000";
    private String sn = "200000000000000";
    private Device device;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        Timestamp registerTime = new Timestamp(System.currentTimeMillis());
        device = new Device();
        device.setId(1L);
        device.setImei(imei);
        device.setSn(sn);
        device.setRegisterTime(registerTime);

        deviceService = Mockito.mock(DeviceService.class);
        controller = new DeviceController(deviceService);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void returnDeviceWhenRegister() throws Exception {
        when(deviceService.register(any())).thenReturn(device);

        Device d = new Device();
        d.setImei(imei);
        d.setSn(sn);
        request = MockMvcRequestBuilders
                .post("/device/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(d))
                .accept(MediaType.APPLICATION_JSON);
        mvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("id").value(device.getId()))
                .andExpect(jsonPath("imei").value(device.getImei()))
                .andExpect(jsonPath("sn").value(device.getSn()))
                .andExpect(jsonPath("registerTime").exists())
                .andReturn();

        verify(deviceService).register(any());
    }

}
