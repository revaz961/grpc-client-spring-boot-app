package com.example.grpcclientspringbootapp.service;

import com.example.grpcclientspringbootapp.dto.StockDto;
import com.example.grpcclientspringbootapp.dto.StockQuoteDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StockProviderService {

    StockQuoteDto getStockQuote(StockDto request);

    List<StockQuoteDto> getListStocksQuotes(List<StockDto> request);

    StockQuoteDto getStocksQuote(List<StockDto> request);

    List<StockQuoteDto> getListStockQuotes(StockDto request);

    List<StockQuoteDto> serverSideStreamingListOfStockPrices(StockDto request);

    void clientSideStreamingGetStatisticsOfStocks(List<StockDto> request);

    void bidirectionalStreamingGetListsStockQuotes(List<StockDto> request) throws InterruptedException;
}
