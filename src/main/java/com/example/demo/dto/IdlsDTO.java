package com.example.demo.dto;

import java.util.List;

/**
 * @author GooRay
 * 创建于 2020/9/24
 */
public class IdlsDTO {

    Integer idls [];


    public IdlsDTO() {
    }

    public IdlsDTO(Integer[] idls) {
        this.idls = idls;
    }

    public Integer[] getIdls() {
        return idls;
    }

    public void setIdls(Integer[] idls) {
        this.idls = idls;
    }
}
