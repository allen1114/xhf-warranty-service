package com.zkztch.xhf.warranty.rest;

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

    private DeviceService deviceService;
    private DeviceController controller;
    private MockHttpServletRequestBuilder request;
    private MockMvc mvc;
    private String imei = "460000000000000";
    private String sn = "200000000000000";
    private Device device;

    @BeforeEach
    public void setup() {
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

        request = MockMvcRequestBuilders
                .post("/device/register")
                .param("imei", imei)
                .param("sn", sn)
                .accept(MediaType.APPLICATION_JSON);
        mvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("id").value(device.getId()))
                .andExpect(jsonPath("imei").value(device.getImei()))
                .andExpect(jsonPath("sn").value(device.getSn()))
                .andExpect(jsonPath("registerTime").exists())
                .andReturn();

        verify(deviceService).register(any());
    }

    @Test
    public void returnDeviceWhenFindByImei() throws Exception {
        when(deviceService.findByImei(imei)).thenReturn(device);
        when(deviceService.findBySn(any())).thenReturn(null);

        request = MockMvcRequestBuilders
                .get("/device/" + imei)
                .accept(MediaType.APPLICATION_JSON);
        mvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("id").value(device.getId()))
                .andExpect(jsonPath("imei").value(device.getImei()))
                .andExpect(jsonPath("sn").value(device.getSn()))
                .andExpect(jsonPath("registerTime").exists())
                .andReturn();
        verify(deviceService).findByImei(imei);
    }

    @Test
    public void returnDeviceWhenFindBySn() throws Exception {
        when(deviceService.findByImei(any())).thenReturn(null);
        when(deviceService.findBySn(sn)).thenReturn(device);
        request = MockMvcRequestBuilders
                .get("/device/" + sn)
                .accept(MediaType.APPLICATION_JSON);
        mvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("id").value(device.getId()))
                .andExpect(jsonPath("imei").value(device.getImei()))
                .andExpect(jsonPath("sn").value(device.getSn()))
                .andExpect(jsonPath("registerTime").exists())
                .andReturn();
        verify(deviceService).findBySn(sn);
    }

    @Test
    public void returnEmptyWhenNotFound() throws Exception {
        when(deviceService.findByImei(any())).thenReturn(null);
        when(deviceService.findBySn(any())).thenReturn(null);
        request = MockMvcRequestBuilders
                .get("/device/" + sn)
                .accept(MediaType.APPLICATION_JSON);
        mvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("id").isEmpty())
                .andExpect(jsonPath("imei").isEmpty())
                .andExpect(jsonPath("sn").isEmpty())
                .andExpect(jsonPath("registerTime").isEmpty())
                .andReturn();
        verify(deviceService).findByImei(sn);
        verify(deviceService).findBySn(sn);
    }

}
