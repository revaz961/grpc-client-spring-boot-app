package com.example.grpcclientspringbootapp.dto;

import lombok.Data;

@Data
public class StockDto {
    private String tickerSymbol;
    private String companyName;
    private String description;
}
