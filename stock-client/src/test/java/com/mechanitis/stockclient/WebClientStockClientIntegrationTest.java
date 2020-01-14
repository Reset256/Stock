package com.mechanitis.stockclient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Objects;

class WebClientStockClientIntegrationTest {

    private WebClient webClient = WebClient.builder().build();

    @Test
    void shouldRetrieveStockPricesFromTheService() {
        WebClientStockClient webClientStockClient = new WebClientStockClient(webClient);
        Flux<StockPrice> prices = webClientStockClient.pricesFor("SYMBOL");

        Assertions.assertNotNull(prices);
        Flux<StockPrice> fivePrices = prices.take(5);
        Assertions.assertEquals(5, fivePrices.count().block());
        Assertions.assertEquals("SYMBOL", Objects.requireNonNull(fivePrices.blockFirst()).getSymbol());
    }

}