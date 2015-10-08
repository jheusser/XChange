package com.xeiam.xchange.gemini.v1;

import si.mazi.rescu.SynchronizedValueFactory;

import com.xeiam.xchange.BaseExchange;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.gemini.v1.service.polling.GeminiAccountService;
import com.xeiam.xchange.gemini.v1.service.polling.GeminiMarketDataService;
import com.xeiam.xchange.gemini.v1.service.polling.GeminiTradeService;
import com.xeiam.xchange.utils.nonce.AtomicLongIncrementalTime2013NonceFactory;

public class GeminiExchange extends BaseExchange implements Exchange {

  private SynchronizedValueFactory<Long> nonceFactory = new AtomicLongIncrementalTime2013NonceFactory();

  @Override
  public void applySpecification(ExchangeSpecification exchangeSpecification) {

    super.applySpecification(exchangeSpecification);

    this.pollingMarketDataService = new GeminiMarketDataService(this);
    this.pollingAccountService = new GeminiAccountService(this);
    this.pollingTradeService = new GeminiTradeService(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass().getCanonicalName());
    exchangeSpecification.setSslUri("https://api.gemini.com/");
    exchangeSpecification.setHost("api.gemini.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("Gemini");
    exchangeSpecification.setExchangeDescription("Gemini is a bitcoin exchange.");

    return exchangeSpecification;
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {

    return nonceFactory;
  }

}
