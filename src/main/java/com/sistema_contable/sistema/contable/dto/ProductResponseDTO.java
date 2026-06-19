package com.sistema_contable.sistema.contable.dto;

import java.util.List;

public class ProductResponseDTO {

    private Long id;
    private String name;
    private Double salePrice;
    private List<LotResponseDTO> lots;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public List<LotResponseDTO> getLots() {
        return lots;
    }

    public void setLots(List<LotResponseDTO> lots) {
        this.lots = lots;
    }
}
