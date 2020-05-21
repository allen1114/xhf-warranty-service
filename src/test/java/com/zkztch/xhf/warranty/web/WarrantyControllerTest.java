package com.zkztch.xhf.warranty.web;


import com.zkztch.xhf.warranty.domain.Device;
import com.zkztch.xhf.warranty.service.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class WarrantyControllerTest {

    private DeviceService deviceService;
    private WarrantyController controller;
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
        controller = new WarrantyController(deviceService);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void whenNoParamReturnInput() throws Exception {

        request = MockMvcRequestBuilders.get("/warranty");

        mvc.perform(request).andExpect(status().isOk())
                .andExpect(view().name("warranty_input"))
                .andReturn();

    }

    @Test
    public void whenDeviceNotExistReturnNotFound() throws Exception {
        when(deviceService.findByImei(any())).thenReturn(null);
        when(deviceService.findBySn(any())).thenReturn(null);

        request = MockMvcRequestBuilders.get("/warranty").param("p", imei);

        mvc.perform(request).andExpect(status().isOk())
                .andExpect(view().name("warranty_notfound"))
                .andReturn();

        verify(deviceService).findByImei(imei);
        verify(deviceService).findBySn(imei);

    }

    @Test
    public void whenDeviceFoundByImeiReturnPeriod() throws Exception {
        when(deviceService.findByImei(imei)).thenReturn(device);
        when(deviceService.findBySn(any())).thenReturn(null);

        request = MockMvcRequestBuilders.get("/warranty").param("p", imei);

        mvc.perform(request).andExpect(status().isOk())
                .andExpect(view().name("warranty_period"))
                .andExpect(model().attributeExists("device"))
                .andExpect(model().attribute("device", device))
                .andReturn();
        verify(deviceService).findByImei(imei);
    }

    @Test
    public void whenDeviceFoundBySnReturnPeriod() throws Exception {
        when(deviceService.findByImei(any())).thenReturn(null);
        when(deviceService.findBySn(sn)).thenReturn(device);

        request = MockMvcRequestBuilders.get("/warranty").param("p", sn);

        mvc.perform(request).andExpect(status().isOk())
                .andExpect(view().name("warranty_period"))
                .andExpect(model().attributeExists("device"))
                .andExpect(model().attribute("device", device))
                .andReturn();
        verify(deviceService).findBySn(sn);
    }
}
