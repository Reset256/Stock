package com.mechanitis.stockui;

import com.mechanitis.stockclient.StockClient;
import com.mechanitis.stockclient.StockPrice;
import com.mechanitis.stockclient.WebClientStockClient;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Consumer;

import static java.lang.String.valueOf;
import static javafx.collections.FXCollections.observableArrayList;


@Component
public class ChartController {
    @FXML
    public LineChart<String, Double> chart;

    private StockClient webClientStockClient;


    public ChartController(StockClient stockClient) {
        this.webClientStockClient = stockClient;
    }

    @FXML
    public void initialize() {
        ObservableList<XYChart.Series<String, Double>> data = observableArrayList();

        Arrays.asList("SYMBOL01", "SYMBOL02", "SYMBOL03").forEach(s -> {
            final PriceSubscriber priceSubscriber = new PriceSubscriber(s);
            webClientStockClient.pricesFor(s).subscribe(priceSubscriber);
            data.add(priceSubscriber.getSeries());
        });

        chart.setData(data);

    }


    private static class PriceSubscriber implements Consumer<StockPrice> {
        private ObservableList<XYChart.Data<String, Double>> seriesData = observableArrayList();
        private final Series<String, Double> series;

        private PriceSubscriber(String symbol) {
            series = new Series<>(symbol, seriesData);
        }

        @Override
        public void accept(StockPrice stockPrice) {
            Platform.runLater(() ->
                    seriesData.add(new Data<>(
                            valueOf(stockPrice.getTime().getSecond()),
                            stockPrice.getPrice())
                    )
            );
        }

        public Series<String, Double> getSeries() {
            return series;
        }
    }
}
