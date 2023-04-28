package com.example.grpcclientspringbootapp.controller;

import com.example.grpcclientspringbootapp.dto.StockDto;
import com.example.grpcclientspringbootapp.dto.StockQuoteDto;
import com.example.grpcclientspringbootapp.service.impl.StockProviderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockController {

    private final StockProviderServiceImpl stockProviderService;

    @PostMapping("/stock-quote")
    public StockQuoteDto getStockQuote(@RequestBody StockDto request) {
        return stockProviderService.getStockQuote(request);
    }

    @PostMapping("/stocks-quotes")
    public List<StockQuoteDto> getListStocksQuotes(@RequestBody List<StockDto> request) {
        return stockProviderService.getListStocksQuotes(request);
    }

    @PostMapping("/stocks-quote")
    public StockQuoteDto getStocksQuote(@RequestBody List<StockDto> request) {
        return stockProviderService.getStocksQuote(request);
    }

    @PostMapping("/stock-quotes")
    public List<StockQuoteDto> getListStockQuotes(@RequestBody StockDto request) {
        return stockProviderService.getListStockQuotes(request);
    }

    @PostMapping("/server-side-stream")
    public List<StockQuoteDto> serverSideStream(@RequestBody StockDto request) {
        return stockProviderService.serverSideStreamingListOfStockPrices(request);
    }

    @PostMapping("/client-side-stream")
    public void clientSideStream(@RequestBody List<StockDto> request) {
        stockProviderService.clientSideStreamingGetStatisticsOfStocks(request);
    }

    @PostMapping("/bidirectional-stream")
    public void bidirectionalStream(@RequestBody List<StockDto> request) throws InterruptedException {
        stockProviderService.bidirectionalStreamingGetListsStockQuotes(request);
    }

}
