package com.xeiam.xchange.gemini.v1.service.polling;

import java.io.IOException;
import java.util.Collection;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.exceptions.ExchangeException;
import com.xeiam.xchange.gemini.v1.dto.GeminiException;
import com.xeiam.xchange.gemini.v1.dto.marketdata.GeminiDepth;
import com.xeiam.xchange.gemini.v1.dto.marketdata.GeminiTicker;
import com.xeiam.xchange.gemini.v1.dto.marketdata.GeminiTrade;

/**
 * <p>
 * Implementation of the market data service for Bitfinex
 * </p>
 * <ul>
 * <li>Provides access to various market data values</li>
 * </ul>
 */
public class GeminiMarketDataServiceRaw extends GeminiBasePollingService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public GeminiMarketDataServiceRaw(Exchange exchange) {

    super(exchange);
  }

  public GeminiTicker getBitfinexTicker(String pair) throws IOException {

    try {
      GeminiTicker bitfinexTicker = bitfinex.getTicker(pair);
      return bitfinexTicker;
    } catch (GeminiException e) {
      throw new ExchangeException(e.getMessage());
    }
  }

  public GeminiDepth getBitfinexOrderBook(String pair, Integer limitBids, Integer limitAsks) throws IOException {

    try {
      GeminiDepth bitfinexDepth;
      if (limitBids == null && limitAsks == null) {
        bitfinexDepth = bitfinex.getBook(pair);
      } else {
        bitfinexDepth = bitfinex.getBook(pair, limitBids, limitAsks);
      }
      return bitfinexDepth;
    } catch (GeminiException e) {
      throw new ExchangeException(e.getMessage());
    }
  }

  public GeminiTrade[] getBitfinexTrades(String pair, long sinceTimestamp) throws IOException {

    try {
      GeminiTrade[] bitfinexTrades = bitfinex.getTrades(pair, sinceTimestamp);
      return bitfinexTrades;
    } catch (GeminiException e) {
      throw new ExchangeException("Bitfinex returned an error: " + e.getMessage());
    }
  }

  public Collection<String> getBitfinexSymbols() throws IOException {

    try {
      return bitfinex.getSymbols();
    } catch (GeminiException e) {
      throw new ExchangeException("Bitfinex returned an error: " + e.getMessage());
    }
  }
}
