package com.zkztch.xhf.warranty.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    @NotNull
    @Size(max = 16)
    @ApiModelProperty(example = "100000000000000")
    private String imei;

    @Column(name = "sn", unique = true, nullable = false, updatable = false)
    @NotNull
    @Size(max = 64)
    @ApiModelProperty(example = "200000000000000")
    private String sn;

    @Column(name = "register_time", updatable = false, nullable = false)
    @CreationTimestamp
    @ApiModelProperty(dataType = "java.lang.String",example = "2020-02-02")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Timestamp registerTime;

}
