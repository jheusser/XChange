package com.xeiam.xchange.btce.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.historical.service.polling.HistoricalPollingMarketDataService;

/**
 * Tests the BTCEAdapter class
 */
public class HistoricalPollingMarketDataServiceTest {

  @Test
  public void testReadTickerAndTick() throws IOException {

	  URL resource = HistoricalPollingMarketDataServiceTest.class.getResource("/ticker/basic_ticker.txt");
	  HistoricalPollingMarketDataService marketDataService = HistoricalPollingMarketDataService.createFromFile(new File(resource.getFile()));
	  
	  marketDataService.getTicker("BTC", "USD");
	  marketDataService.tick();
	  Ticker ticker = marketDataService.getTicker("BTC", "USD");

	  assertEquals(ticker.getAsk(), BigMoney.of(CurrencyUnit.USD, new BigDecimal("159.10483")));
  }

  @Test
  public void testCheckAvailableDataPoints() throws IOException {
	  URL resource = HistoricalPollingMarketDataServiceTest.class.getResource("/ticker/basic_ticker.txt");
	  HistoricalPollingMarketDataService marketDataService = HistoricalPollingMarketDataService.createFromFile(new File(resource.getFile()));
	  
	  List<Class<?>> availableMarketData = marketDataService.getAvailableMarketData();
	  assertEquals(availableMarketData, Lists.<Class<?>>newArrayList(Ticker.class));
  } 
  
}
