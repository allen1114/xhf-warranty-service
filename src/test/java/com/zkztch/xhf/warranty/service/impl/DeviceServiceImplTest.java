package com.zkztch.xhf.warranty.service.impl;

import com.zkztch.xhf.warranty.domain.Device;
import com.zkztch.xhf.warranty.repository.DeviceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.mockito.Mockito.*;

public class DeviceServiceImplTest {

    private DeviceRepository deviceRepository;
    private DeviceServiceImpl deviceService;
    private Device device;
    private String imei = "460000000000000";
    private String sn = "200000000000000";

    @BeforeEach
    public void setup() {
        deviceRepository = mock(DeviceRepository.class);
        deviceService = new DeviceServiceImpl(deviceRepository);

        Timestamp registerTime = new Timestamp(System.currentTimeMillis());
        device = new Device();
        device.setId(1L);
        device.setImei(imei);
        device.setSn(sn);
        device.setRegisterTime(registerTime);
    }

    @Test
    public void createWhenNotFound() {
        when(deviceRepository.findByImei(imei)).thenReturn(null);
        when(deviceRepository.findBySn(sn)).thenReturn(null);
        when(deviceRepository.save(any())).thenReturn(device);

        Device d = new Device();
        d.setImei(imei);
        d.setSn(sn);

        Device deviceRegistered = deviceService.register(d);

        verify(deviceRepository).save(any());
        verify(deviceRepository).findByImei(imei);
        verify(deviceRepository).findBySn(sn);

        Assertions.assertEquals(device, deviceRegistered);
    }

    @Test
    public void returnFoundBySn() {
        when(deviceRepository.findByImei(imei)).thenReturn(null);
        when(deviceRepository.findBySn(sn)).thenReturn(device);
        when(deviceRepository.save(any())).thenReturn(null);

        Device d = new Device();
        d.setImei(imei);
        d.setSn(sn);

        Device deviceRegistered = deviceService.register(d);

        verify(deviceRepository).findBySn(sn);

        Assertions.assertEquals(device, deviceRegistered);
    }

    @Test
    public void returnFoundByImei() {
        when(deviceRepository.findByImei(imei)).thenReturn(device);
        when(deviceRepository.findBySn(sn)).thenReturn(null);
        when(deviceRepository.save(any())).thenReturn(null);

        Device d = new Device();
        d.setImei(imei);
        d.setSn(sn);

        Device deviceRegistered = deviceService.register(d);

        verify(deviceRepository).findByImei(imei);

        Assertions.assertEquals(device, deviceRegistered);
    }

    @Test
    public void foundByImei() {
        when(deviceRepository.findByImei(imei)).thenReturn(device);
        Device d = deviceService.findByImei(imei);
        verify(deviceRepository).findByImei(imei);
        Assertions.assertEquals(device, d);
    }

    @Test
    public void foundBySn() {
        when(deviceRepository.findBySn(sn)).thenReturn(device);
        Device d = deviceService.findBySn(sn);
        verify(deviceRepository).findBySn(sn);
        Assertions.assertEquals(device, d);
    }

}
