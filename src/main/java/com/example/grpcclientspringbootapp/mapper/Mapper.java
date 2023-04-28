package com.example.grpcclientspringbootapp.mapper;

import com.example.grpc.server.spring.boot.app.stock.Stock;
import com.example.grpc.server.spring.boot.app.stock.StockQuote;
import com.example.grpc.server.spring.boot.app.stock.StockQuotes;
import com.example.grpc.server.spring.boot.app.stock.Stocks;
import com.example.grpcclientspringbootapp.dto.StockDto;
import com.example.grpcclientspringbootapp.dto.StockQuoteDto;

import java.util.List;

public class Mapper {
    public static Stock dtoToStock(StockDto dto) {
        return Stock.newBuilder()
                .setTickerSymbol(dto.getTickerSymbol())
                .setCompanyName(dto.getCompanyName())
                .setDescription(dto.getDescription())
                .build();
    }

    public static Stocks dtosToStocks(List<StockDto> stocksDto) {
        Stocks.Builder builder = Stocks.newBuilder();
        stocksDto.forEach(dto -> builder.addStock(Stock.newBuilder()
                .setTickerSymbol(dto.getTickerSymbol())
                .setCompanyName(dto.getCompanyName())
                .setDescription(dto.getDescription())
                .build())
        );
        return builder.build();
    }

    public static StockQuoteDto stockQuoteToDto(StockQuote stockQuote) {
        return new StockQuoteDto(stockQuote);
    }

    public static List<StockQuoteDto> stockQuotesToDtoList(StockQuotes stockQuotes) {
        return stockQuotes.getStockQuotesList().stream().map(StockQuoteDto::new).toList();
    }


}
