package com.example.grpcclientspringbootapp.service.impl;

import com.example.grpc.server.spring.boot.app.stock.Stock;
import com.example.grpc.server.spring.boot.app.stock.StockQuote;
import com.example.grpc.server.spring.boot.app.stock.StockQuoteProviderGrpc;
import com.example.grpc.server.spring.boot.app.stock.Stocks;
import com.example.grpcclientspringbootapp.dto.StockDto;
import com.example.grpcclientspringbootapp.dto.StockQuoteDto;
import com.example.grpcclientspringbootapp.service.StockProviderService;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.grpcclientspringbootapp.mapper.Mapper.dtoToStock;
import static com.example.grpcclientspringbootapp.mapper.Mapper.dtosToStocks;
import static com.example.grpcclientspringbootapp.mapper.Mapper.stockQuoteToDto;
import static com.example.grpcclientspringbootapp.mapper.Mapper.stockQuotesToDtoList;

@Slf4j
@Service
public class StockProviderServiceImpl implements StockProviderService {
    @GrpcClient("stock-quote-provider")
    private StockQuoteProviderGrpc.StockQuoteProviderBlockingStub blockingStub;
    @GrpcClient("stock-quote-provider")
    private StockQuoteProviderGrpc.StockQuoteProviderStub nonBlockingStub;

    @Override
    public StockQuoteDto getStockQuote(StockDto request) {
        return new StockQuoteDto(blockingStub.getStockQuote(dtoToStock(request)));
    }

    @Override
    public List<StockQuoteDto> getListStocksQuotes(List<StockDto> request) {
        return stockQuotesToDtoList(blockingStub.getListStocksQuotes(dtosToStocks(request)));
    }

    @Override
    public StockQuoteDto getStocksQuote(List<StockDto> request) {
        return stockQuoteToDto(blockingStub.getStocksQuote(dtosToStocks(request)));
    }

    @Override
    public List<StockQuoteDto> getListStockQuotes(StockDto request) {
        return stockQuotesToDtoList(blockingStub.getListStockQuotes(dtoToStock(request)));
    }

    @Override
    public List<StockQuoteDto> serverSideStreamingListOfStockPrices(StockDto request) {
        List<StockQuoteDto> result = new ArrayList<>();
        Iterator<StockQuote> stockQuoteIterator;
        try {
            log.info("REQUEST - ticker symbol {}", request.getTickerSymbol());
            stockQuoteIterator = blockingStub.serverSideStreamingGetListStockQuotes(dtoToStock(request));
            for (int i = 1; stockQuoteIterator.hasNext(); i++) {
                StockQuote stockQuote = stockQuoteIterator.next();
                result.add(stockQuoteToDto(stockQuote));
                log.info("RESPONSE - Price #{}: {}", i, stockQuote.getPrice());
            }
        } catch (StatusRuntimeException e) {
            log.error("RPC failed: {}", e.getStatus().getDescription());
        }
        return result;
    }

    @Override
    public void clientSideStreamingGetStatisticsOfStocks(List<StockDto> request) {
        StreamObserver<StockQuote> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(StockQuote summary) {
                log.info("RESPONSE, got stock statistics - Average Price: {}, description: {}",
                        summary.getPrice(), summary.getDescription());
            }

            @Override
            public void onError(Throwable t) {
                log.error(t.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("Finished clientSideStreamingGetStatisticsOfStocks");
            }
        };

        StreamObserver<Stock> requestObserver =
                nonBlockingStub.clientSideStreamingGetStatisticsOfStocks(responseObserver);
        Stocks stocks = dtosToStocks(request);
        try {
            for (Stock stock : stocks.getStockList()) {
                log.info("REQUEST: {}, {}", stock.getTickerSymbol(), stock.getCompanyName());
                requestObserver.onNext(stock);
            }
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            throw e;
        }
        requestObserver.onCompleted();
    }

    @Override
    public void bidirectionalStreamingGetListsStockQuotes(List<StockDto> request) throws InterruptedException {
        StreamObserver<StockQuote> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(StockQuote stockQuote) {
                log.info("RESPONSE price# {} : {}, description: {}", stockQuote.getOfferNumber(),
                        stockQuote.getPrice(), stockQuote.getDescription());
            }

            @Override
            public void onError(Throwable t) {
                log.error(t.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("Finished bidirectionalStreamingGetListsStockQuotes");
            }

        };

        StreamObserver<Stock> requestObserver =
                nonBlockingStub.bidirectionalStreamingGetListsStockQuotes(responseObserver);
        Stocks stocks = dtosToStocks(request);
        try {
            for (Stock stock : stocks.getStockList()) {
                log.info("REQUEST: {}, {}", stock.getTickerSymbol(), stock.getCompanyName());
                requestObserver.onNext(stock);
                Thread.sleep(200);
            }
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            throw e;
        }
        requestObserver.onCompleted();
    }
}
