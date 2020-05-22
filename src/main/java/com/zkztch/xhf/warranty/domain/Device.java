package com.zkztch.xhf.warranty.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zkztch.xhf.warranty.Consts;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "t_device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    @NotNull
    @Size(max = 16)
    private String imei;

    @Column(name = "sn", unique = true, nullable = false, updatable = false)
    @NotNull
    @Size(max = 64)
    private String sn;

    @Column(name = "register_time", updatable = false, nullable = false)
    @CreationTimestamp
    @ApiModelProperty(dataType = "java.lang.String")
    private Timestamp registerTime;

    @JsonIgnore
    public Timestamp getWarrantyPeriod() {
        return new Timestamp(registerTime.getTime() + Consts.WARRANTY_PERIOD);
    }
}
