package com.zkztch.xhf.warranty.repository;

import com.zkztch.xhf.warranty.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeviceRepository extends JpaRepository<Device, Long>, JpaSpecificationExecutor<Device> {

    Device findBySn(String sn);

    Device findByImei(String imei);

}
