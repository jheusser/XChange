package com.xeiam.xchange.gemini.v1.service.polling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestProxyFactory;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.gemini.v1.GeminiAdapters;
import com.xeiam.xchange.gemini.v1.GeminiAuthenticated;
import com.xeiam.xchange.gemini.v1.service.GeminiHmacPostBodyDigest;
import com.xeiam.xchange.gemini.v1.service.GeminiPayloadDigest;
import com.xeiam.xchange.service.BaseExchangeService;
import com.xeiam.xchange.service.polling.BasePollingService;

public class GeminiBasePollingService extends BaseExchangeService implements BasePollingService {

  protected final String apiKey;
  protected final GeminiAuthenticated bitfinex;
  protected final ParamsDigest signatureCreator;
  protected final ParamsDigest payloadCreator;

  /**
   * Constructor
   *
   * @param exchange
   */
  public GeminiBasePollingService(Exchange exchange) {

    super(exchange);

    this.bitfinex = RestProxyFactory.createProxy(GeminiAuthenticated.class, exchange.getExchangeSpecification().getSslUri());
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.signatureCreator = GeminiHmacPostBodyDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
    this.payloadCreator = new GeminiPayloadDigest();
  }

  @Override
  public List<CurrencyPair> getExchangeSymbols() throws IOException {

    List<CurrencyPair> currencyPairs = new ArrayList<CurrencyPair>();
    for (String symbol : bitfinex.getSymbols()) {
      currencyPairs.add(GeminiAdapters.adaptCurrencyPair(symbol));
    }
    return currencyPairs;
  }
}
