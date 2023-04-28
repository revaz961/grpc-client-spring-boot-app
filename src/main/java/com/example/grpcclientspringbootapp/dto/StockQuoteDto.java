package com.example.grpcclientspringbootapp.dto;

import com.example.grpc.server.spring.boot.app.stock.StockQuote;
import lombok.Data;

@Data
public class StockQuoteDto {
    private double price;
    private int offerNumber;
    private String description;

    public StockQuoteDto(StockQuote stockQuote) {
        this.price = stockQuote.getPrice();
        this.offerNumber = stockQuote.getOfferNumber();
        this.description = stockQuote.getDescription();
    }
}
